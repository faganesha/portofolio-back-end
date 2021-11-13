package com.example.Library.Controller;

import com.example.Library.Mapper.BodyResponse.Error.ErrorMessageResponse;
import com.example.Library.Mapper.Response.BorrowmentResponse;
import com.example.Library.Service.BorrowmentService;
import com.example.Library.Service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    private ExcelService service;

    @Autowired
    private BorrowmentService borrowmentService;

    @GetMapping("/users/export/excel")
    public ResponseEntity exportToExcel(@RequestParam("from") String from, @RequestParam("until") String until, HttpServletResponse response) throws IOException {

        Date javaDateFrom =null;
        Date javaDateUntil = null;

        try {
            String dateFrom = from;
            String dateUntil = until;
             javaDateFrom = Date.valueOf(from);
             javaDateUntil = Date.valueOf(until);
        } catch (Exception e) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Due date");
            error.setMessage("Date format must be yyyy-mm-dd, example: 2020-12-31");
            return ResponseEntity.status(HttpStatus.OK).body(error);
        }

        List<BorrowmentResponse> borrowmentList = borrowmentService.showAll(javaDateFrom, javaDateUntil);

        service.export(javaDateFrom, javaDateUntil);

        return ResponseEntity.status(HttpStatus.OK).body("true");
    }
}
