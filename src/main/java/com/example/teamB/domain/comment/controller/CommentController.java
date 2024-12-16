package com.example.teamB.domain.comment.controller;

import com.example.teamB.domain.comment.dto.CommentResponseDto;
import com.example.teamB.domain.comment.service.command.CommentCommandService;
import com.example.teamB.domain.comment.service.query.CommentQueryService;
import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@PostMapping
	@Operation(summary = "댓글 생성 API", description = "댓글 생성 API입니다. 대댓글이 아닌경우 parentId를 0으로, 대댓글인경우 바로 위 댓글의 id를 parentId로 넘겨주세요!, postId는 게시글 ID입니다")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
	})
	public CustomResponse<CommentResponseDto.CommentPreviewDTO> createComment (
			@CurrentMember Member member,
			@PathVariable Long postId,
			@Valid @RequestBody CommentCreateRequestDto request) {

		CommentResponseDto.CommentPreviewDTO response = commentCommandService.createComment(request, member, postId);

		return CustomResponse.onSuccess(response);
	}

	@Operation(summary = "댓글 수정 API", description = "댓글 수정 API입니다. 댓글 ID와 바꿀 내용을 보내주세요")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
	})
	@PostMapping("/{commentId}")
	public CustomResponse<CommentResponseDto.CommentPreviewDTO > updateComment (
			@CurrentMember Member member,
			@PathVariable Long commentId,
			@Valid @RequestBody CommentUpdateRequestDto request) {

		CommentResponseDto.CommentPreviewDTO response = commentCommandService.updateComment(commentId, request, member.getId());

		return CustomResponse.onSuccess(response);
	}

	@Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API입니다. 삭제할 댓글 ID를 보내주세요")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
	})
	@DeleteMapping("/{commentId}")
	public CustomResponse<Void> deleteComment(
			@CurrentMember Member member,
			@PathVariable Long commentId) {

		commentCommandService.deleteComment(commentId, member.getId());

		return CustomResponse.onSuccess(null);
	}

	@Operation(summary = "댓글 조회 API", description = "댓글 조회 API입니다. cursor값은 초기에는 0, 댓글을 한번 이상 받아온경우 dto에 lastId로 전달된 값을 보내주세요, 한번에 가져오는 댓글의 양 기본값은 10개입니다!")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
	})
	@GetMapping
	public CustomResponse<CommentResponseDto.CommentPreviewListDTO> getComments(
		@PathVariable Long postId,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "10") int size) {

		CommentResponseDto.CommentPreviewListDTO responses = commentQueryService.getComments(postId, cursor, size);

		return CustomResponse.onSuccess(responses);
	}

}
