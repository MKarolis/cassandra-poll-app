package com.karolismed.cassandra.polling.app.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    @NotEmpty(message = "Options not specified")
    List<UUID> optionIds;
}
