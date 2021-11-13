package com.example.Library.Service;

import com.example.Library.Mapper.Request.BorrowmentRequest;
import com.example.Library.Mapper.Response.BorrowResponse;
import com.example.Library.Mapper.Response.BorrowmentResponse;
import com.example.Library.Mapper.Response.TopBookResponse;
import com.example.Library.Model.Book;
import com.example.Library.Model.Borrowment;
import com.example.Library.Model.Member;
import com.example.Library.Projection.TopBookProjection;
import com.example.Library.Projection.TopFineProjection;
import com.example.Library.Projection.TopMemberProjection;
import com.example.Library.Repository.BookRepository;
import com.example.Library.Repository.BorrowmentRepository;
import com.example.Library.Repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BorrowmentService {
    @Autowired
    BorrowmentRepository borrowmentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BookRepository bookRepository;

    public Borrowment isBook(String bookId) {
        return borrowmentRepository.findFirstByReturnFlagAndBookId(0, bookId).orElse(null);
    }

    public Borrowment isBookLate(String bookId) {
        return borrowmentRepository.findFirstByReturnFlagAndBookId(2, bookId).orElse(null);
    }

    public Optional<Borrowment> checkBorrowment(Long param) {
        return borrowmentRepository.findById(param);
    }

    public Member checkMember(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Book checkBook(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<BorrowmentResponse> convert(List<Borrowment> models) {
        List<BorrowmentResponse> responses = new ArrayList<>();

        for (Borrowment borrowment : models) {
            BorrowmentResponse response = new ObjectMapper().convertValue(borrowment, BorrowmentResponse.class);
            Long tempSec = (borrowment.getDueDate().getTime() - borrowment.getCreatedAt().getTime());
            int day = (int) TimeUnit.MILLISECONDS.toDays(tempSec);
            response.setDuration(day + " hari");

            LocalDate dateNow = LocalDate.now();
            LocalDate dateReturned = borrowment.getDueDate().toLocalDate();

            int fine = 10000;
            Integer totalFine = 0;
            String fineResponse = "-";

            Period period = Period.between(dateReturned, dateNow);
            int late = period.getDays();

            if (late > 0 && borrowment.getReturnFlag() != 3) {
                totalFine = late * fine;

                fineResponse = totalFine.toString();
                response.setReturnFlag(2);
                borrowment.setFine(totalFine);
                borrowment.setReturnFlag(2);
                borrowmentRepository.save(borrowment);
            } else if (borrowment.getReturnFlag() == 2 || borrowment.getReturnFlag() == 3) {
                fineResponse = Integer.toString(borrowment.getFine());
            }
            response.setFine(fineResponse);
            responses.add(response);
        }


        return responses;
    }

    public List<BorrowmentResponse> showAll(Date from, Date until) {
        return convert(borrowmentRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAt(from, until));
    }

    public BorrowmentResponse showDetail(Long borrowmentId) {
        ObjectMapper mapper = new ObjectMapper();
        Borrowment result = borrowmentRepository.findById(borrowmentId).get();

        Long tempSec = (result.getDueDate().getTime() - result.getCreatedAt().getTime());
        int day = (int) TimeUnit.MILLISECONDS.toDays(tempSec);

//        mapper.convertValue(result, BorrowmentResponse.class);
        BorrowmentResponse response = mapper.convertValue(result, BorrowmentResponse.class);
        response.setDuration(day + " hari");
        response.setCreatedAt(result.getCreatedAt());

        System.out.println(result.getCreatedAt());

        LocalDate dateNow = LocalDate.now();
        LocalDate dateReturned = result.getDueDate().toLocalDate();

        int fine = 10000;
        Integer totalFine = 0;
        String fineResponse = "-";

        Period period = Period.between(dateReturned, dateNow);
        int late = period.getDays();
        System.out.println("late: " + late);

        if (late > 0) {
            totalFine = late * fine;

            fineResponse = totalFine.toString();
            response.setReturnFlag(2);
            result.setFine(totalFine);
            result.setReturnFlag(2);
            borrowmentRepository.save(result);
        }
        response.setFine(fineResponse);
        System.out.println(response);
        return response;

    }

    public BorrowResponse borrow(BorrowmentRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        Borrowment borrowment = new Borrowment();
        borrowment.setBook(checkBook(request.getBookId()));
        borrowment.setMember(checkMember(request.getMemberId()));
        borrowment.setDueDate(Date.valueOf(request.getDueDate()));

        Book book = checkBook(request.getBookId());
        book.setStock(book.getStock() - 1);

        BorrowResponse response = new BorrowResponse();

        try {
            borrowmentRepository.save(borrowment);
            bookRepository.save(book);

            response = mapper.convertValue(borrowment, BorrowResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    public Borrowment checkBorrowMemberBook(String bookId, Long memberId) {
        return borrowmentRepository.findByMemberIdAndBookIdAndReturnFlag(memberId, bookId, 0).orElse(null);
    }

    public Borrowment checkBorrowMemberBookLate(String bookId, Long memberId) {
        return borrowmentRepository.findByMemberIdAndBookIdAndReturnFlag(memberId, bookId, 2).orElse(null);
    }

    public Borrowment isReturned(String bookId) {
        return borrowmentRepository.findByAndBookIdAndReturnFlag(bookId, 0).orElse(null);
    }

    public Borrowment isReturnedLate(String bookId) {
        return borrowmentRepository.findByAndBookIdAndReturnFlag(bookId, 2).orElse(null);
    }

    public Borrowment checkReturnMemberBook(String bookId, Long memberId) {
        return borrowmentRepository.findByMemberIdAndBookIdAndReturnFlag(memberId, bookId, 1).orElse(null);
    }

    public List<TopBookProjection> topBook() {
        return borrowmentRepository.topBook();
    }

    public List<TopMemberProjection> topMember() {
        return borrowmentRepository.topMember();
    }

    public List<TopFineProjection> topFine() {
        return borrowmentRepository.topFine();
    }

    public List<TopBookResponse> convertTop(List<TopBookProjection> models) {
        List<TopBookResponse> responses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (TopBookProjection model : models) {
            TopBookResponse response = mapper.convertValue(model, TopBookResponse.class);
            System.out.println(model);
            responses.add(response);
        }
        return responses;
    }

    public boolean returnBook(Long memberId, String bookId) {
        Borrowment borrowment = checkBorrowMemberBook(bookId, memberId);
        Book book = checkBook(bookId);
        try {
            if (borrowment.getReturnFlag() == 0) {
                borrowment.setReturnFlag(1);
            } else if (borrowment.getReturnFlag() == 2) {
                borrowment.setReturnFlag(3);
            }

            book.setStock(book.getStock() + 1);

            borrowmentRepository.save(borrowment);
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean returnBookLate(Long memberId, String bookId) {
        Borrowment borrowment = checkBorrowMemberBookLate(bookId, memberId);
        Book book = checkBook(bookId);
        try {
            if (borrowment.getReturnFlag() == 0) {
                borrowment.setReturnFlag(1);
            } else if (borrowment.getReturnFlag() == 2) {
                borrowment.setReturnFlag(3);
            }

            book.setStock(book.getStock() + 1);

            borrowmentRepository.save(borrowment);
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}



