package com.karolismed.cassandra.polling.app.auth.service;

import com.karolismed.cassandra.polling.app.auth.dto.RegisterRequestDto;
import com.karolismed.cassandra.polling.app.auth.model.User;
import com.karolismed.cassandra.polling.app.core.exception.ConflictException;
import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final CassandraOperations cassandraTemplate;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        String cql = String.format("SELECT * FROM users WHERE username = '%s'", username);

        return Optional.ofNullable(
            cassandraTemplate.selectOne(cql, User.class)
        );
    }

    public void registerUser(RegisterRequestDto registerRequestDto) {
        User user = User.builder()
            .username(registerRequestDto.getUsername())
            .password(passwordEncoder.encode(registerRequestDto.getPassword()))
            .build();
        boolean writeApplied = cassandraTemplate.insert(
            user, InsertOptions.builder().withIfNotExists().build()
        ).wasApplied();

        if (!writeApplied) {
            throw new ConflictException(
                String.format("User with username %s already exists", user.getUsername())
            );
        }
    }
}
