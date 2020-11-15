package com.karolismed.cassandra.polling.app.poll.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("votes_by_pollId")
public class VotesByPollId {

    @PrimaryKeyColumn(name = "pollId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID pollId;
    @PrimaryKeyColumn(name = "optionId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID optionId;
    @PrimaryKeyColumn(name = "username", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String username;
    private String title;
}
