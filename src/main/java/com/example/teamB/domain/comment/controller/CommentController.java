package com.example.teamB.domain.comment.controller;

import java.util.List;

import com.example.teamB.domain.comment.dto.CommentResponseDTO;
import com.example.teamB.domain.comment.service.command.CommentCommandService;
import com.example.teamB.domain.comment.service.query.CommentQueryService;
import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.global.apiPayload.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentCommandService commentCommandService;
	private final CommentQueryService commentQueryService;

	// 댓글 생성 API
	@PostMapping
	public CustomResponse<CommentResponseDTO.CommentPreviewDTO> createComment (
			@CurrentMember Member member,
			@PathVariable Long postId,
			@Valid @RequestBody CommentCreateRequestDto request) {

		CommentResponseDTO.CommentPreviewDTO response = commentCommandService.createComment(request, member, postId);

		return CustomResponse.onSuccess(response);
	}
	// 댓글 수정 API
	@PostMapping("/{commentId}")
	public CustomResponse<CommentResponseDTO.CommentPreviewDTO > updateComment (
			@CurrentMember Member member,
			@PathVariable Long commentId,
			@Valid @RequestBody CommentUpdateRequestDto request) {

		CommentResponseDTO.CommentPreviewDTO response = commentCommandService.updateComment(commentId, request, member.getId());

		return CustomResponse.onSuccess(response);
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public CustomResponse<Void> deleteComment(
			@CurrentMember Member member,
			@PathVariable Long commentId) {

		commentCommandService.deleteComment(commentId, member.getId());

		return CustomResponse.onSuccess(null);
	}

	// 댓글 조회 (커서 기반 페이지네이션)
	@GetMapping
	public CustomResponse<CommentResponseDTO.CommentPreviewListDTO> getComments(
		@PathVariable Long postId,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size) {

		CommentResponseDTO.CommentPreviewListDTO responses = commentQueryService.getComments(postId, cursor, size);

		return CustomResponse.onSuccess(responses);
	}

}
