package com.example.Library.Repository;

import com.example.Library.Model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByFlag(int flag);
    List<Member> findByFlagOrderById(int flag);
    List<Member> findByFlagOrderByName(int flag);
    Optional<Member> findByFlagAndIdContainingOrNameContaining(int flag, Long id, String tittle);
    Optional<Member> findByFlagAndId(int flag, Long id);
}
