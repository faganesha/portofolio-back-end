package com.example.Library.Validator;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.BodyResponse.Error.ErrorMessageResponse;
import com.example.Library.Mapper.Request.BookRequest;
import com.example.Library.Model.Book;
import com.example.Library.Model.Borrowment;
import com.example.Library.Model.Category;
import com.example.Library.Service.BookService;
import com.example.Library.Service.BorrowmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookValidator {
    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowmentService borrowmentService;

    public ErrorBodyResponse addBookValidator(BookRequest request) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        Book checkBook = bookService.checkBook(request.getId());
        Category checkCategory = bookService.getCategory(request.getCategoryId()).orElse(null);

        if (request.getId().trim().isEmpty() || request.getId() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("ID cannot be empty");
            errorMessages.add(error);
        }

        if (checkBook != null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("ID already registered");
            errorMessages.add(error);
        }

        if (request.getAuthor().trim().isEmpty() || request.getAuthor() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Author");
            error.setMessage("Author cannot be empty");
            errorMessages.add(error);
        }

        if (request.getStock() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Stock");
            error.setMessage("Stock cannot be empty");
            errorMessages.add(error);
        }

        if (request.getStock() != null && request.getStock() < 0) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Stock");
            error.setMessage("Stock must be greater than or equal to 0");
            errorMessages.add(error);
        }

        if (request.getTittle().trim().isEmpty() || request.getTittle() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Tittle");
            error.setMessage("Tittle cannot be empty");
            errorMessages.add(error);
        }

        if (request.getCategoryId() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Category");
            error.setMessage("Category cannot be empty");
            errorMessages.add(error);
        }

        if (checkCategory == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Category");
            error.setMessage("Category not found");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Please check input parameter");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }
        return errors;
    }

    public ErrorBodyResponse showDetailBookValidator(String bookId) {
        Book checkBook = bookService.checkBook(bookId);
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        if (checkBook == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book not found");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Data has 0 result");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }

        return errors;
    }

    public ErrorBodyResponse deleteBookValidator(BookRequest request) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();
        Book checkBook = bookService.checkBook(request.getId());

        Borrowment borrowment = borrowmentService.isBook(request.getId());
        Borrowment borrowmentLate = borrowmentService.isBookLate(request.getId());

        if(request.getId().trim().isEmpty() || request.getId() == null){
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book Id cannot be empty");
            errorMessages.add(error);
        }

        if (borrowment != null || borrowmentLate != null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book is borrowed, make sure all book is returned before deleting the book");
            errorMessages.add(error);
        }

        if (checkBook == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book not found");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Data has 0 result");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }

        return errors;
    }

    public ErrorBodyResponse editBookValidator(BookRequest request) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        Book checkBook = bookService.checkBook(request.getId());
        Category checkCategory = bookService.getCategory(request.getCategoryId()).orElse(null);

        if (request.getId().trim().isEmpty() || request.getId() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("ID cannot be empty");
            errorMessages.add(error);
        }

        if (checkBook == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book not found");
            errorMessages.add(error);
        }

        if (request.getAuthor().trim().isEmpty() || request.getAuthor() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Author");
            error.setMessage("Author cannot be empty");
            errorMessages.add(error);
        }

        if (request.getStock() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Stock");
            error.setMessage("Stock cannot be empty");
            errorMessages.add(error);
        }

        if (request.getStock() != null && request.getStock() < 0) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Stock");
            error.setMessage("Stock must be greater than or equal to 0");
            errorMessages.add(error);
        }

        if (request.getTittle().trim().isEmpty() || request.getTittle() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Tittle");
            error.setMessage("Tittle cannot be empty");
            errorMessages.add(error);
        }

        if (request.getCategoryId() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Category");
            error.setMessage("Category cannot be empty");
            errorMessages.add(error);
        }

        if (checkCategory == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Category");
            error.setMessage("Category not found");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Please check input parameter");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }
        return errors;
    }
}
