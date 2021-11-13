package com.example.Library.Mapper.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class BorrowResponse {
    private BookResponse book;
    private MemberResponse member;
    @JsonProperty("due_date")
    @JsonFormat(pattern="dd-MM-yyyy")
    private Timestamp dueDate;
    @JsonProperty("borrow_day")
    @JsonFormat(pattern="dd-MM-yyyy")
    private Timestamp createdAt;
}
