package com.example.teamB.domain.ootd.repository;

import com.example.teamB.domain.ootd.entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OotdRepository extends JpaRepository<Ootd, Long> {

    @Query("SELECT o FROM Ootd o WHERE o.member.id = :memberId AND o.createdAt BETWEEN :startOfMonth AND :endOfMonth")
    List<Ootd> findByMemberIdAndCreatedAtBetween(@Param("memberId") Long memberId,
                                                 @Param("startOfMonth") LocalDateTime startOfMonth,
                                                 @Param("endOfMonth") LocalDateTime endOfMonth);
}
