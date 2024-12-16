package com.example.teamB.domain.comment.dto;

import com.example.teamB.domain.comment.entity.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

	@NotBlank(message = "댓글 내용은 필수 입니다.")
	private String content;

	private Long parentId;

	public CommentCreateRequestDto(String content, Long parentId) {
		this.content = content;
		this.parentId = parentId;
	}
}
