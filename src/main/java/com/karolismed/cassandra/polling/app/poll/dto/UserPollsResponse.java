package com.karolismed.cassandra.polling.app.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPollsResponse {
    @Builder.Default
    private List<UserPollDto> polls = new ArrayList<>();
}
