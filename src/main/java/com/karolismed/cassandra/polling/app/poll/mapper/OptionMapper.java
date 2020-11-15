package com.karolismed.cassandra.polling.app.poll.mapper;

import com.karolismed.cassandra.polling.app.poll.dto.OptionDto;
import com.karolismed.cassandra.polling.app.poll.model.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionMapper {
    public static OptionDto map(Option option) {
        return OptionDto.builder()
            .id(option.getId())
            .value(option.getTitle())
            .build();
    }
}
