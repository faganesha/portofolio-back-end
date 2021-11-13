package com.example.Library.Mapper.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class BorrowmentResponse {
    private BookResponse book;
    private MemberResponse member;
    private String duration;

    @JsonProperty("due_date")
    @JsonFormat(pattern = "dd-MMMM-yyyy", timezone = "Asia/Jakarta")
    private Date dueDate;

    @JsonProperty("borrow_day")
    @JsonFormat(pattern = "dd-MMMM-yyyy", timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonProperty("return_status")
    private int returnFlag;

    @JsonProperty("fine")
    private String fine;

    //lebih baik pakai json getter
    @JsonProperty("return_status")
    public String getReturnFlag() {
        String status = "";
        if (returnFlag == 1) {
            status = "Sudah dikembalikan";
        } else if (returnFlag == 2) {
            status = "Terlambat dikembalikan";
        } else if (returnFlag == 0) {
            status = "Belum jatuh tempo";
        } else  if (returnFlag == 3) {
            status = "Sudah dikembalikan dan dikenakan denda";
        }
        return status;
    }

}
