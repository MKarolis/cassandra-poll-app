package com.karolismed.cassandra.polling.app.poll.service;

import com.karolismed.cassandra.polling.app.config.security.UserDetailsImpl;
import com.karolismed.cassandra.polling.app.core.exception.BadRequestException;
import com.karolismed.cassandra.polling.app.core.exception.ConflictException;
import com.karolismed.cassandra.polling.app.core.exception.ForbiddenException;
import com.karolismed.cassandra.polling.app.core.exception.NotFoundException;
import com.karolismed.cassandra.polling.app.poll.dto.CreatePollDto;
import com.karolismed.cassandra.polling.app.poll.dto.CreatePollResponse;
import com.karolismed.cassandra.polling.app.poll.dto.OptionVoteResultsDto;
import com.karolismed.cassandra.polling.app.poll.dto.PollDto;
import com.karolismed.cassandra.polling.app.poll.dto.PollVoteResultsDto;
import com.karolismed.cassandra.polling.app.poll.dto.UserPollDto;
import com.karolismed.cassandra.polling.app.poll.dto.UserPollsResponse;
import com.karolismed.cassandra.polling.app.poll.dto.VoteRequest;
import com.karolismed.cassandra.polling.app.poll.mapper.PollMapper;
import com.karolismed.cassandra.polling.app.poll.model.Option;
import com.karolismed.cassandra.polling.app.poll.model.Poll;
import com.karolismed.cassandra.polling.app.poll.model.PollByUsername;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.karolismed.cassandra.polling.app.poll.model.PollVoteByOption;
import com.karolismed.cassandra.polling.app.poll.model.UserVotedPollWithCount;
import com.karolismed.cassandra.polling.app.poll.model.VotesByPollId;
import com.karolismed.cassandra.polling.app.poll.model.VotesByUser;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PollService {

    private final CassandraTemplate cassandraTemplate;
    private final AsyncCassandraTemplate asyncCassandraTemplate;

    public CreatePollResponse createPoll(CreatePollDto createPollDto, UserDetailsImpl userDetails) {
        Instant creationTime = Instant.now();
        Poll poll = Poll.builder()
            .id(UUID.randomUUID())
            .createdBy(userDetails.getUsername())
            .title(createPollDto.getTitle())
            .timestamp(creationTime)
            .build();
        List<Option> options = createPollDto.getOptions().stream()
            .map(o -> Option.builder().pollId(poll.getId()).id(UUID.randomUUID()).title(o).build())
            .collect(Collectors.toList());

        cassandraTemplate.insert(poll);
        cassandraTemplate.insert(PollByUsername.fromPoll(poll));
        CassandraBatchOperations batch = cassandraTemplate.batchOps();
        batch.insert(options);
        batch.execute();

        return CreatePollResponse.builder().id(poll.getId()).build();
    }

    public PollDto getPollById(UUID pollId) {
        Poll poll = getByIdOrThrow(pollId);
        List<Option> options = getPollOptions(pollId);

        return PollMapper.map(poll, options);
    }

    public UserPollsResponse getUserPolls(String username) {
        String pollsCql = String.format(
            "SELECT * FROM poll_by_username WHERE createdBy = '%s' ORDER BY timestamp DESC",
            username
        );
        List<Poll> polls = cassandraTemplate.select(pollsCql, Poll.class);

        List<Pair<Poll, CompletableFuture<Integer>>> pollsWithPendingCounts = new ArrayList<>();
        polls.forEach(poll -> {
            String voteCountCql = String.format(
                "SELECT count(*) FROM votes_by_pollId WHERE pollId = %s",
                poll.getId()
            );
            pollsWithPendingCounts.add(
                Pair.of(poll, asyncCassandraTemplate.selectOne(voteCountCql, Integer.class).completable())
            );
        });

        UserPollsResponse userPollsResponse = new UserPollsResponse();
        pollsWithPendingCounts.forEach(pollWithCount -> {
            userPollsResponse.getPolls().add(
                UserPollDto.builder()
                    .id(pollWithCount.getLeft().getId())
                    .title(pollWithCount.getLeft().getTitle())
                    .voteCount(pollWithCount.getRight().join())
                    .build()
            );
        });

        return userPollsResponse;
    }

    public UserPollsResponse getUserVotedPolls(String username) {
        String pollsCql = String.format(
            "SELECT pollId, pollTitle, count(optionId) as voteCount " +
                "FROM votes_by_user WHERE username = '%s' " +
                "GROUP BY pollId, pollTitle",
            username
        );

        List<UserVotedPollWithCount> userPolls = cassandraTemplate.select(pollsCql, UserVotedPollWithCount.class);

        List<UserPollDto> mappedUserPolls = userPolls.stream()
            .map(poll ->
                UserPollDto.builder()
                    .id(poll.getPollId())
                    .title(poll.getPollTitle())
                    .voteCount(poll.getVoteCount())
                    .build()
            ).collect(Collectors.toList());

        return UserPollsResponse.builder()
            .polls(mappedUserPolls)
            .build();
    }

    public void vote(UUID pollId, VoteRequest voteRequest, UserDetailsImpl userDetails) {
        Poll poll = getByIdOrThrow(pollId);
        String matchingOptionsCql = String.format(
            "SELECT * FROM options WHERE pollId = %s AND id IN (%s)",
            pollId, voteRequest.getOptionIds().stream().map(UUID::toString).collect(Collectors.joining(","))
        );

        List<Option> options = cassandraTemplate.select(matchingOptionsCql, Option.class);
        if (options.size() != voteRequest.getOptionIds().size()) {
            throw new BadRequestException("Invalid option ids provided");
        }

        CassandraBatchOperations votesByPollBatch = cassandraTemplate.batchOps();
        CassandraBatchOperations votesByUserBatch = cassandraTemplate.batchOps();
        options.forEach(option -> {
            VotesByPollId votesByPollId = VotesByPollId.builder()
                .pollId(pollId)
                .optionId(option.getId())
                .title(option.getTitle())
                .username(userDetails.getUsername())
                .build();
            votesByPollBatch.insert(List.of(votesByPollId), InsertOptions.builder().withIfNotExists().build());

            VotesByUser votesByUser = VotesByUser.builder()
                .pollId(pollId)
                .pollTitle(poll.getTitle())
                .optionId(option.getId())
                .username(userDetails.getUsername())
                .build();
            votesByUserBatch.insert(List.of(votesByUser), InsertOptions.builder().withIfNotExists().build());
        });
        boolean wasApplied = votesByPollBatch.execute().wasApplied();
        if (!wasApplied) {
            throw new ConflictException("Voting failed because some options were voted for already");
        }
        votesByUserBatch.execute();
    }

    public PollVoteResultsDto getPollResults(UUID pollId, String username) {
        Poll poll = getByIdOrThrow(pollId);
        if (!poll.getCreatedBy().equals(username)) {
            throw new ForbiddenException("User does not have access to this poll's results");
        }

        String resultsCql = String.format(
            "SELECT optionId, username, title FROM votes_by_pollId WHERE pollId = %s", pollId
        );

        Map<Pair<UUID, String>, List<String>> optionVotedUsersMap =
            cassandraTemplate.select(resultsCql, PollVoteByOption.class).stream()
                .collect(Collectors.groupingBy(
                    opt -> Pair.of(opt.getOptionId(), opt.getTitle()),
                    Collectors.mapping(PollVoteByOption::getUsername, Collectors.toList())
                ));

        int totalVoteCount = optionVotedUsersMap.values().stream().mapToInt(List::size).sum();

        return PollVoteResultsDto.builder()
            .pollId(pollId)
            .title(poll.getTitle())
            .totalVotes(totalVoteCount)
            .optionVotes(
                optionVotedUsersMap.entrySet().stream().map(entry ->
                    OptionVoteResultsDto.builder()
                        .optionId(entry.getKey().getLeft())
                        .value(entry.getKey().getRight())
                        .votedUsers(entry.getValue())
                        .votes(entry.getValue().size())
                        .percentage(BigDecimal.valueOf(
                            (double)entry.getValue().size() * 100 / totalVoteCount
                        ).setScale(2, RoundingMode.HALF_UP))
                        .build()
                ).collect(Collectors.toList())
            )
            .build();
    }

    private List<Option> getPollOptions(UUID pollId) {
        String cql = String.format("SELECT * FROM options WHERE pollId = %s", pollId);
        return cassandraTemplate.select(cql, Option.class);
    }

    private Poll getByIdOrThrow(UUID pollId) {
        String cql = String.format("SELECT * FROM polls WHERE id = %s", pollId);
        return Optional.ofNullable(cassandraTemplate.selectOne(cql, Poll.class))
            .orElseThrow(() -> new NotFoundException(String.format("Poll with id %s does not exist", pollId)));
    }
}
