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

public class BorrowmentRequest {
    @JsonProperty("book_id")
    private String bookId;
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("due_date")
    private String dueDate;
}
