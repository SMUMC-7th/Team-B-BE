package com.example.teamB.domain.comment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentResponseDto;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.domain.comment.service.CommentService;
import com.example.teamB.global.apiPayload.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	// 댓글 생성 API
	@PostMapping
	public CustomResponse<CommentResponseDto> createCommet (
		@PathVariable Long postId,
		@Valid @RequestBody CommentCreateRequestDto request,
		@RequestHeader("Member-Id") Long memberId) {

		CommentResponseDto response = commentService.createComment(request, memberId, postId);

		return CustomResponse.onSuccess(response);
	}
	// 댓글 수정 API
	@PostMapping("/{commentId}")
	public CustomResponse<CommentResponseDto> updateComment (
		@PathVariable Long commentId,
		@Valid @RequestBody CommentUpdateRequestDto request,
		@RequestHeader("Member-Id") Long memberId) {

		CommentResponseDto response = commentService.updateComment(commentId, request, memberId);
		return CustomResponse.onSuccess(response);
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public CustomResponse<Void> deleteComment(
		@PathVariable Long commentId,
		@RequestHeader("Member-Id") Long memberId) {

		commentService.deleteComment(commentId, memberId);
		return CustomResponse.onSuccess(null);
	}

	// 댓글 조회 (커서 기반 페이지네이션)
	@GetMapping
	public CustomResponse<List<CommentResponseDto>> getComments(
		@PathVariable Long postId,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size) {

		List<CommentResponseDto> responses = commentService.getComments(postId, cursor, size);
		return CustomResponse.onSuccess(responses);
	}

}
