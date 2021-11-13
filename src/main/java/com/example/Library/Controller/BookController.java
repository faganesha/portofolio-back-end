package com.example.Library.Controller;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.BodyResponse.SuccessResponseMap;
import com.example.Library.Mapper.Request.BookRequest;
import com.example.Library.Mapper.Response.BookResponse;
import com.example.Library.Service.BookService;
import com.example.Library.Service.BorrowmentService;
import com.example.Library.Validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/book")

public class BookController {
    @Autowired
    private BookService service;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private BorrowmentService borrowmentService;

    @GetMapping
    public ResponseEntity showAllBook(@RequestParam("order") Integer order) {
        List<BookResponse> result = service.showAllBook(order);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity showBook(@PathVariable("bookId") String bookId) {
        ErrorBodyResponse errorMessage = bookValidator.showDetailBookValidator(bookId);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            BookResponse result = service.showBook(bookId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity addBook(@RequestBody BookRequest request) {
        ErrorBodyResponse errorMessage = bookValidator.addBookValidator(request);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            BookResponse result = service.addBook(request);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody BookRequest request) {
        ErrorBodyResponse errorMessage = bookValidator.deleteBookValidator(request);

        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            boolean success = service.deleteBook(request.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseMap("Buku dengan " + "ID: " + request.getId() + " berhasil dihapus"));
        }
    }

    @PutMapping(path = "/edit")
    public ResponseEntity edit(@RequestBody BookRequest request) {
        ErrorBodyResponse errorMessage = bookValidator.editBookValidator(request);

        if(errorMessage != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            BookResponse result = service.editBook(request);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }
}
