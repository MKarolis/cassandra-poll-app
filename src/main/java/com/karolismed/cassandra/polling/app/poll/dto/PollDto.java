package com.karolismed.cassandra.polling.app.poll.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private UUID id;
    private String title;
    private Instant dateCreated;
    private List<OptionDto> options;
    private String owner;
}
