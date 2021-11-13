package com.example.Library.Controller;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.BodyResponse.ErrorResponseMap;
import com.example.Library.Mapper.BodyResponse.SuccessResponseMap;
import com.example.Library.Mapper.Request.BorrowmentRequest;
import com.example.Library.Mapper.Response.BorrowResponse;
import com.example.Library.Mapper.Response.BorrowmentResponse;
import com.example.Library.Model.Borrowment;
import com.example.Library.Projection.TopBookProjection;
import com.example.Library.Projection.TopFineProjection;
import com.example.Library.Projection.TopMemberProjection;
import com.example.Library.Service.BorrowmentService;
import com.example.Library.Service.MemberService;
import com.example.Library.Validator.BorrowmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/borrowment")

public class BorrowmentController {
    @Autowired
    private BorrowmentService service;

    @Autowired
    private BorrowmentValidator borrowmentValidator;

    @Autowired
    private MemberService memberService;

    @PutMapping
    public ResponseEntity showAllBorrowment(Date from, Date until) {
        //sorting
        List<BorrowmentResponse> result = service.showAll(from, until);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(path = "/{borrowmentId}")
    public ResponseEntity showBorrowment(@PathVariable("borrowmentId") Long borrowmentId) {
        ErrorBodyResponse errorMessage = borrowmentValidator.showBorrowmentDetailValidator(borrowmentId);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            BorrowmentResponse result = service.showDetail(borrowmentId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @PostMapping(path = "/borrow")
    public ResponseEntity borrow(@RequestBody BorrowmentRequest request) {
        ErrorBodyResponse errorMessage = borrowmentValidator.borrowBookValidator(request);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            BorrowResponse result = service.borrow(request);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @PutMapping(path = "/return")
    public ResponseEntity returnBook(@RequestBody BorrowmentRequest request) {
        ErrorBodyResponse errorMessage = borrowmentValidator.returnBookValidator(request);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            Borrowment isBorrow = service.checkBorrowMemberBook(request.getBookId(), request.getMemberId());
            Borrowment isBorrowLate = service.checkBorrowMemberBookLate(request.getBookId(), request.getMemberId());
            boolean result = false;

            if (isBorrow != null) {
                result = service.returnBook(request.getMemberId(), request.getBookId());
                if (result) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new SuccessResponseMap("Buku " + isBorrow.getBook().getTittle() + " berhasil dikembalikan" +
                                    " oleh " + isBorrow.getMember().getName() + " pada tanggal " + isBorrow.getUpdatedAt()));
                }
            } else if (isBorrowLate != null) {
                result = service.returnBookLate(request.getMemberId(), request.getBookId());
                if (result) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new SuccessResponseMap("Buku " + isBorrowLate.getBook().getTittle() + " berhasil dikembalikan" +
                                    " oleh " + isBorrowLate.getMember().getName() + " pada tanggal " + isBorrowLate.getUpdatedAt() +
                                    " dengan denda " + isBorrowLate.getFine()));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseMap("Failed to return book"));
    }


    @GetMapping(path = "/top/book")
    public ResponseEntity topBook() {
        List<TopBookProjection> result = service.topBook();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(path = "/top/member")
    public ResponseEntity topMember() {
        List<TopMemberProjection> result = service.topMember();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(path = "/top/fine")
    public ResponseEntity topFine() {
        List<TopFineProjection> result = service.topFine();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
//---------------------------------------------------------------------------------------------------
}
