package com.example.teamB.domain.comment.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.teamB.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Slice<Comment> findAllByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime cursor, Pageable pageable);
}
