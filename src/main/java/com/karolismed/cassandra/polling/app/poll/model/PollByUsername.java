package com.karolismed.cassandra.polling.app.poll.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("poll_by_username")
public class PollByUsername {

    @PrimaryKeyColumn(name = "createdBy", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String createdBy;
    @PrimaryKeyColumn(name = "timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Instant timestamp;
    private UUID id;
    private String title;

    public static PollByUsername fromPoll(Poll poll) {
        return PollByUsername.builder()
            .id(poll.getId())
            .createdBy(poll.getCreatedBy())
            .timestamp(poll.getTimestamp())
            .title(poll.getTitle())
            .build();
    }
}
