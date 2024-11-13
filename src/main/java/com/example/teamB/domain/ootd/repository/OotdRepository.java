package com.example.teamB.domain.ootd.repository;

import com.example.teamB.domain.ootd.entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OotdRepository extends JpaRepository<Ootd, Long> {
}
