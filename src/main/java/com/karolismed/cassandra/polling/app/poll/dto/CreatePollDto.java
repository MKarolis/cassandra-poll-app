package com.karolismed.cassandra.polling.app.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePollDto {
    @Size(min = 1, max = 250, message = "Title's length can be between 1 and 250")
    private String title;
    @Size(min = 1, max = 100, message = "From 1 to 100 options are allowed")
    private List<String> options;
}
