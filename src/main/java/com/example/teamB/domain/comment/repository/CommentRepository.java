package com.example.teamB.domain.comment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.teamB.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	// 커서 기반 페이지네이션 - 최상위 댓글 조회
	@Query("SELECT c FROM Comment c " +
			"WHERE c.post.id = :postId AND c.parent IS NULL " +
			"AND c.id > :cursorId " +
			"ORDER BY c.createdAt ASC, c.id ASC")
	List<Comment> findTopLevelCommentsByCursor(@Param("postId") Long postId, @Param("cursorId") Long cursorId, Pageable pageable);


	// 한 번의 쿼리로 여러 댓글의 대댓글을 모두 조회
	@Query("SELECT c FROM Comment c WHERE c.parent IN :parentComments ORDER BY c.createdAt ASC")
	List<Comment> findAllChildrenByParentComments(@Param("parentComments") List<Comment> parentComments);

	Optional<Comment> findByIdAndMemberId(Long id, Long memberId);
}
