package com.example.teamB.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 댓글 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {
	@NotBlank(message = "수정된 댓글 내용은 필수입니다.")
	private String content;

	public CommentUpdateRequestDto(String content) {
		this.content = content;
	}
}
