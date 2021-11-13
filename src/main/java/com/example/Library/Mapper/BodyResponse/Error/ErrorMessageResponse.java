package com.example.Library.Mapper.BodyResponse.Error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ErrorMessageResponse {
    private String field;
    @JsonProperty("error_message")
    private String message;
}
