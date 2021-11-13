package com.example.Library.Validator;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.BodyResponse.Error.ErrorMessageResponse;
import com.example.Library.Mapper.Request.BorrowmentRequest;
import com.example.Library.Model.Book;
import com.example.Library.Model.Borrowment;
import com.example.Library.Model.Member;
import com.example.Library.Repository.BorrowmentRepository;
import com.example.Library.Service.BorrowmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BorrowmentValidator {
    @Autowired
    BorrowmentRepository borrowmentRepository;

    @Autowired
    BorrowmentService borrowmentService;

    public ErrorBodyResponse showBorrowmentDetailValidator(Long borrowmentId) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        Optional<Borrowment> check = borrowmentService.checkBorrowment(borrowmentId);

        if (!check.isPresent()) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Borrowment Id");
            error.setMessage("Borrowment cannot be found");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Please check Borrowment Id");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }
        return errors;
    }

    public ErrorMessageResponse isDueDateEmpty(BorrowmentRequest request) {
        if (request.getDueDate() == null || request.getDueDate().trim().isEmpty()  ) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Due date");
            error.setMessage("Due date cannot be empty");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse dueDateFormatValidator(BorrowmentRequest request) {
        try {
            String strDate = request.getDueDate();
            Date javaDate = Date.valueOf(strDate);
            return null;
        } catch (Exception e) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Due date");
            error.setMessage("Date format must be yyyy-mm-dd, example: 2020-12-31");
            return error;
        }
    }

    public ErrorMessageResponse dueDateValidator(BorrowmentRequest request) {

        if (request.getDueDate() != null && dueDateFormatValidator(request) == null) {
            Date javaDate = Date.valueOf(request.getDueDate());
            Calendar cal = Calendar.getInstance();
            long dateNow = cal.getTime().getTime();
            long dateReturned = javaDate.getTime();


            int borrowTime = (int) (dateReturned - dateNow);
            int day = (int) TimeUnit.MILLISECONDS.toDays(borrowTime) + 1;


            if (day > 30) {
                ErrorMessageResponse error = new ErrorMessageResponse();
                error.setField("Due date");
                error.setMessage("The duration of the borrowment cannot be more than 30 days");
                return error;
            } else if (dateNow == dateReturned) {
                ErrorMessageResponse error = new ErrorMessageResponse();
                error.setField("Due date");
                error.setMessage("The return date cannot be the same as today");
                return error;
            } else if (dateReturned < dateNow) {
                ErrorMessageResponse error = new ErrorMessageResponse();
                error.setField("Due date");
                error.setMessage("The duration of the borrowment cannot be less than 1 day");
                return error;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ErrorMessageResponse isMemberIdEmpty(BorrowmentRequest request) {
        if (request.getMemberId() == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Member Id");
            error.setMessage("Member Id cannot be empty");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse isMemberFound(BorrowmentRequest request) {
        Member checkMember = borrowmentService.checkMember(request.getMemberId());

        if (checkMember == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Member Id");
            error.setMessage("Member has not registered");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse isMemberBorrowedBook(BorrowmentRequest request) {
        Borrowment isBorrowed = borrowmentService.checkBorrowMemberBook(request.getBookId(), request.getMemberId());

        if (isBorrowed != null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Member and Book");
            error.setMessage("Member already borrow this book");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse isBookIdEmpty(BorrowmentRequest request) {
        if (request.getBookId() == null || request.getBookId().trim().isEmpty()) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book Id cannot be empty");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse checkStock(BorrowmentRequest request) {
        Book checkBook = borrowmentService.checkBook(request.getBookId());

        if (request.getBookId()!= null && checkBook != null &&checkBook.getStock() <= 0) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Stock");
            error.setMessage("There are no stock for this book");
            return error;
        } else {
            return null;
        }
    }

    public ErrorMessageResponse isBookFound(BorrowmentRequest request) {
        Book checkBook = borrowmentService.checkBook(request.getBookId());

        if (checkBook == null) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id");
            error.setMessage("Book not found");
            return error;
        } else {
            return null;
        }
    }

    public ErrorBodyResponse borrowBookValidator(BorrowmentRequest request) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        //check data isempty
        if (isBookIdEmpty(request) != null) {
            errorMessages.add(isBookIdEmpty(request));
        }

        if (isDueDateEmpty(request) != null) {
            errorMessages.add(isDueDateEmpty(request));
        }

        if (isMemberIdEmpty(request) != null) {
            errorMessages.add(isMemberIdEmpty(request));
        }

        //check date format
        if (isDueDateEmpty(request) == null && dueDateFormatValidator(request) != null) {
            errorMessages.add(dueDateFormatValidator(request));
        }

        //check duration
        if (isDueDateEmpty(request) == null && dueDateValidator(request) != null) {
            errorMessages.add(dueDateValidator(request));
        }

        //check stock available
        if (isBookIdEmpty(request) == null && checkStock(request) != null) {
            errorMessages.add(checkStock(request));
        }

        //check if member already borrow the book
        if (isBookIdEmpty(request) == null && isMemberIdEmpty(request) == null && isMemberBorrowedBook(request) != null) {
            errorMessages.add(isMemberBorrowedBook(request));
        }

        //check if member register
        if (isMemberIdEmpty(request) == null && isMemberFound(request) != null) {
            errorMessages.add(isMemberFound(request));
        }

        //check id book match
        if (isBookIdEmpty(request) == null && isBookFound(request) != null) {
            errorMessages.add(isBookFound(request));
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

    public ErrorBodyResponse returnBookValidator(BorrowmentRequest request) {
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        Borrowment isBorrow = borrowmentService.checkBorrowMemberBook(request.getBookId(), request.getMemberId());
        Borrowment isBorrowLate = borrowmentService.checkBorrowMemberBookLate(request.getBookId(), request.getMemberId());

        if(isBorrowLate == null && isBorrow== null){
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Book Id & Member Id");
            error.setMessage("Member is not borrowing the book");
            errorMessages.add(error);
        }

        //check data isempty
        if (isBookIdEmpty(request) != null) {
            errorMessages.add(isBookIdEmpty(request));
        }

        if (isMemberIdEmpty(request) != null) {
            errorMessages.add(isMemberIdEmpty(request));
        }

        //check if member register
        if (isMemberIdEmpty(request) == null && isMemberFound(request) != null) {
            errorMessages.add(isMemberFound(request));
        }

        //check id book match
        if (isBookIdEmpty(request) == null && isBookFound(request) != null) {
            errorMessages.add(isBookFound(request));
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
