package com.example.teamB.domain.hashtag.repository;

import com.example.teamB.domain.hashtag.entity.Hashtag;
import com.example.teamB.domain.hashtag.enums.HashtagCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByCategory(HashtagCategory category);
}
