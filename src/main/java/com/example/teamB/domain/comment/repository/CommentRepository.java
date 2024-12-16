package com.example.teamB.domain.comment.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.teamB.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	// 특정 게시글의 댓글 목록을 커서 기반 페이지네이션으로 조회
	Slice<Comment> findByPostIdAndIdLessThanOrderByIdDesc(Long postId, Long cursor, Pageable pageable);

	Optional<Comment> findByIdAndMemberId(Long id, Long memberId); // 댓글 작성자 확인용
}
