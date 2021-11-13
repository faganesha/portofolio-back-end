package com.example.Library.Mapper.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class BookRequest {
    private String author, tittle;
    @JsonProperty("category_id")
    private Long categoryId;
    private int flag;
    private Integer stock;

    @JsonProperty("id")
    private String id;
}
