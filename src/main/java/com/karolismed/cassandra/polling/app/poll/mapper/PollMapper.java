package com.karolismed.cassandra.polling.app.poll.mapper;

import com.karolismed.cassandra.polling.app.poll.dto.PollDto;
import com.karolismed.cassandra.polling.app.poll.model.Option;
import com.karolismed.cassandra.polling.app.poll.model.Poll;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PollMapper {

//    public static PollVoteResultsDto map(PollVoteResultsAggregation resultsAggregation) {
//        return PollVoteResultsDto.builder()
//            .pollId(resultsAggregation.getPollId())
//            .title(resultsAggregation.getTitle())
//            .totalVotes(resultsAggregation.getTotalVoteCount())
//            .optionVotes(
//                resultsAggregation.getVoteStats().stream().map(
//                    option -> OptionVoteResultsDto.builder()
//                        .optionId(option.getOptionId())
//                        .value(option.getValue())
//                        .votes(option.getVotes())
//                        .percentage(BigDecimal.valueOf(option.getPercentage()).setScale(2, RoundingMode.HALF_UP))
//                        .build()
//                ).collect(Collectors.toList())
//            )
//            .build();
//    }

    public static PollDto map(Poll poll, List<Option> options) {
        return PollDto.builder()
            .id(poll.getId())
            .dateCreated(poll.getTimestamp())
            .title(poll.getTitle())
            .options(options.stream().map(OptionMapper::map).collect(Collectors.toList()))
            .owner(poll.getCreatedBy())
            .build();
    }
}
