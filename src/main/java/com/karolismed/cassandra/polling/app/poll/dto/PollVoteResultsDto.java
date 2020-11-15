package com.karolismed.cassandra.polling.app.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollVoteResultsDto {
    private UUID pollId;
    private String title;
    private int totalVotes;

    @Builder.Default
    private List<OptionVoteResultsDto> optionVotes = new ArrayList<>();
}
