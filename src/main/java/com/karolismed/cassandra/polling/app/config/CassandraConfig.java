package com.karolismed.cassandra.polling.app.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    public String getContactPoints() {
        return "127.0.0.1";
    }

    @Override
    public int getPort() {
        return 9042;
    }

    @Override
    public String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    public String getKeyspaceName() {
        return "poll_kp";
    }

    @Bean
    AsyncCassandraTemplate asyncCassandraTemplate(CqlSession session) {
        return new AsyncCassandraTemplate(session);
    }
}
