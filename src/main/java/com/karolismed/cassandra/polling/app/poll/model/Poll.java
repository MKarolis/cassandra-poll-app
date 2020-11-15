package com.karolismed.cassandra.polling.app.poll.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("polls")
public class Poll {

    @PrimaryKey
    private UUID id;
    private String createdBy;
    private String title;
    private Instant timestamp;
}
