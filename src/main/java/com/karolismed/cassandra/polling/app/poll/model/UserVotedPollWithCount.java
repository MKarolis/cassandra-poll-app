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
public class UserVotedPollWithCount {
    private UUID pollId;
    private String pollTitle;
    private int voteCount;
}
