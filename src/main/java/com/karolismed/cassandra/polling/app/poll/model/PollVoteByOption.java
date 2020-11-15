package com.karolismed.cassandra.polling.app.poll.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollVoteByOption {
    private UUID optionId;
    private String username;
    private String title;
}
