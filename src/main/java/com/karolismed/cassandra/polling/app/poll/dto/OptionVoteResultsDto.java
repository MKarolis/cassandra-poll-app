package com.karolismed.cassandra.polling.app.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionVoteResultsDto {
    private UUID optionId;
    private String value;
    private int votes;
    private List<String> votedUsers;
    private BigDecimal percentage;
}
