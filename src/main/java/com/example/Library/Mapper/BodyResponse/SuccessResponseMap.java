package com.example.Library.Mapper.BodyResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SuccessResponseMap {
    @JsonProperty("success_message")
    private String successMessage;
}
