package com.example.Library.Mapper.BodyResponse.Error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ErrorBodyResponse {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("total_error")
    private Integer totalError;
    @JsonProperty("error_message")
    List<ErrorMessageResponse> errorMessages;
}
