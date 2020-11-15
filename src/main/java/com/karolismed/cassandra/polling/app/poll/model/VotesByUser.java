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
@Table("votes_by_user")
public class VotesByUser {

    @PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String username;
    @PrimaryKeyColumn(name = "pollId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID pollId;
    @PrimaryKeyColumn(name = "pollTitle", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String pollTitle;
    @PrimaryKeyColumn(name = "optionId", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    private UUID optionId;
}
