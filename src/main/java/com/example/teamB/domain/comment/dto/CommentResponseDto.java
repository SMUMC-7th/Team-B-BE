package com.example.teamB.domain.comment.dto;

import java.time.LocalDateTime;

import com.example.teamB.domain.comment.entity.Comment;

import lombok.Getter;

/**
 * 댓글 응답 DTO
 */
@Getter
public class CommentResponseDto {
	private Long id;

	private String content;

	private Long parentId;

	private Long memberId;

	private String memberNickname;

	private LocalDateTime createdAt;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();

		this.content = comment.getContent();

		this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;

		this.memberId = comment.getMember().getId();

		this.memberNickname = comment.getMember().getNickname();

		this.createdAt = comment.getCreatedAt();
	}
}
