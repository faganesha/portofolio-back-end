package com.example.Library.Repository;

import com.example.Library.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByFlagOrderByAuthor(int flag);
    List<Book> findByFlagOrderByCategory(int flag);
    List<Book> findByFlagOrderByTittle(int flag);
    List<Book> findByFlagAndIdContainingOrTittleContaining(int flag, String id, String tittle);
    Book findByFlagAndIdOrTittle(int flag, String id, String tittle);
    Optional<Book> findById(String id);
}
