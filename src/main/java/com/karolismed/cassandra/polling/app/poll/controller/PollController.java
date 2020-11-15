package com.karolismed.cassandra.polling.app.poll.controller;

import com.karolismed.cassandra.polling.app.config.security.UserDetailsImpl;
import com.karolismed.cassandra.polling.app.poll.dto.CreatePollDto;
import com.karolismed.cassandra.polling.app.poll.dto.CreatePollResponse;
import com.karolismed.cassandra.polling.app.poll.dto.PollDto;
import com.karolismed.cassandra.polling.app.poll.dto.PollVoteResultsDto;
import com.karolismed.cassandra.polling.app.poll.dto.UserPollsResponse;
import com.karolismed.cassandra.polling.app.poll.dto.VoteRequest;
import com.karolismed.cassandra.polling.app.poll.service.PollService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/polls")
@AllArgsConstructor
public class PollController {

    private final PollService pollService;

    @GetMapping("/{pollId}")
    public PollDto getPoll(@PathVariable UUID pollId) {
        return pollService.getPollById(pollId);
    }

    @GetMapping("/{pollId}/results")
    @PreAuthorize("hasAuthority('MANAGE_POLLING')")
    public PollVoteResultsDto getPollResults(
        @PathVariable UUID pollId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return pollService.getPollResults(pollId, userDetails.getUsername());
    }

    @PostMapping("/{pollId}/vote")
    @PreAuthorize("hasAuthority('MANAGE_POLLING')")
    public void vote(
        @PathVariable UUID pollId,
        @Valid @RequestBody VoteRequest voteRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        pollService.vote(pollId, voteRequest, userDetails);
    }

    @GetMapping("/mine/created")
    @PreAuthorize("hasAuthority('MANAGE_POLLING')")
    public UserPollsResponse getUserPolls(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return pollService.getUserPolls(userDetails.getUsername());
    }

    @GetMapping("/mine/voted")
    @PreAuthorize("hasAuthority('MANAGE_POLLING')")
    public UserPollsResponse getUserVotedPolls(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return pollService.getUserVotedPolls(userDetails.getUsername());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_POLLING')")
    public CreatePollResponse createPoll(
        @Valid @RequestBody CreatePollDto createPollDto, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return pollService.createPoll(createPollDto, userDetails);
    }
}
