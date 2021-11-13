package com.example.Library.Repository;

import com.example.Library.Model.Borrowment;
import com.example.Library.Projection.TopBookProjection;
import com.example.Library.Projection.TopFineProjection;
import com.example.Library.Projection.TopMemberProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowmentRepository extends JpaRepository<Borrowment, Long> {
    Optional<Borrowment> findByMemberIdAndBookIdAndReturnFlag(Long memberId, String bookId, int flag);
    Optional<Borrowment> findByAndBookIdAndReturnFlag(String bookId, int flag);
    List<Borrowment> findByReturnFlag(int flag);
    List<Borrowment> findByEmailFlag(int flag);

    List<Borrowment> findByEmailFlagAndReturnFlag(int flag, int returnFlag);

    List<Borrowment> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAt(Date from, Date until);

    //findfirstby jangan pakai list

    Optional<Borrowment> findFirstByReturnFlagAndBookId(int flag, String bookId);

//dalam bulan tertentu
    @Query(value = "SELECT books.tittle as book, " +
            "COUNT(borrowments.book_id) as frequency " +
            "FROM borrowments " +
            "INNER JOIN books ON books.id = borrowments.book_id " +
            "GROUP BY  borrowments.book_id " +
            "ORDER BY frequency DESC " +
            "LIMIT 5", nativeQuery = true)
    List<TopBookProjection> topBook();

    @Query(value = "SELECT members.name as member, MONTHNAME(borrowments.created_at) as month, " +
            "COUNT(borrowments.member_id) as total " +
            "FROM borrowments " +
            "INNER JOIN members ON members.id = borrowments.member_id " +
            "WHERE MONTH(borrowments.created_at) = MONTH(CURRENT_DATE()) " +
            "GROUP BY  borrowments.member_id " +
            "ORDER BY total DESC " +
            "LIMIT 3", nativeQuery = true)
    List<TopMemberProjection> topMember();

    @Query(value = "SELECT members.name as member," +
            "COUNT(borrowments.return_flag) as frequency," +
            "SUM(borrowments.fine) as fine " +
            "FROM borrowments " +
            "INNER JOIN members ON members.id = borrowments.member_id " +
            "GROUP BY  borrowments.member_id " +
            "ORDER BY frequency DESC " +
            "LIMIT 3", nativeQuery = true)
    List<TopFineProjection> topFine();


}
