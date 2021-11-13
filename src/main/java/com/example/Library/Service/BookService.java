package com.example.Library.Service;

import com.example.Library.Mapper.Request.BookRequest;
import com.example.Library.Mapper.Response.BookResponse;
import com.example.Library.Model.Book;
import com.example.Library.Model.Category;
import com.example.Library.Repository.BookRepository;
import com.example.Library.Repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BorrowmentService borrowmentService;

    public List<BookResponse> convert(List<Book> models) {
        List<BookResponse> responses = new ArrayList<>();

        for (Book book : models) {
            BookResponse response = new ObjectMapper().convertValue(book, BookResponse.class);
            responses.add(response);
        }
        return responses;
    }

    public List<Book> checkListBook(String param) {
        return repository.findByFlagAndIdContainingOrTittleContaining(1, param, param);
    }

    public Book checkBook(String param) {
        return repository.findByFlagAndIdOrTittle(1, param, param);
    }

    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    public List<BookResponse> showAllBook(int order) {
        if (order == 1) {
            return convert(repository.findByFlagOrderByTittle(1));
        } else if (order == 2) {
            return convert(repository.findByFlagOrderByCategory(1));
        } else {
            return convert(repository.findByFlagOrderByAuthor(1));
        }
    }

    public BookResponse showBook(String bookId) {
        ObjectMapper mapper = new ObjectMapper();
        Book book = repository.findByFlagAndIdOrTittle(1, bookId, bookId);

        return mapper.convertValue(book, BookResponse.class);
    }

    public BookResponse addBook(BookRequest request) {
        Book book = new Book();
        book.setId(request.getId());
        book.setAuthor(request.getAuthor());
        //langsung nama kategori
        book.setCategory(getCategory(request.getCategoryId()).get());
        book.setTittle(request.getTittle());
        book.setStock(request.getStock());
        //set flag default value
        book.setFlag(1);
        repository.save(book);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(book, BookResponse.class);
    }

    public boolean deleteBook(String id) {
        Book book = checkBook(id);
        if (book != null) {
            book.setFlag(0);
            repository.save(book);
            return true;
        } else {
            return false;
        }
    }

    public BookResponse editBook(BookRequest request) {
        Book book = checkBook(request.getId());
        if (book != null) {
            book.setCategory(getCategory(request.getCategoryId()).get());
            book.setAuthor(request.getAuthor());
            book.setTittle(request.getTittle());
            book.setStock(request.getStock());
            repository.save(book);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(book, BookResponse.class);
    }
}
