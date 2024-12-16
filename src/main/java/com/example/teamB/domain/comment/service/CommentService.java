package com.example.teamB.domain.comment.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.teamB.domain.comment.converter.CommentConverter;
import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentResponseDto;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.domain.comment.entity.Comment;
import com.example.teamB.domain.comment.repository.CommentRepository;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.domain.post.repository.PostRepository;
import com.example.teamB.global.apiPayload.code.GeneralErrorCode;
import com.example.teamB.global.apiPayload.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final CommentConverter commentConverter;

	// 댓글 생성
	public CommentResponseDto createComment(CommentCreateRequestDto request, Long memberId, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

		Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

		Comment parent = null;
		if(request.getParentId() != null) {
			parent = commentRepository.findById(request.getParentId()).orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
		}

		Comment comment = new Comment(request.getContent(), member, post, parent
		);
		// 생성 후 저장
		Comment savedComment = commentRepository.save(comment);

		return commentConverter.toResponse(savedComment);
	}

	// 댓글 수정
	public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto request, Long memberId) {
		Comment comment = commentRepository.findByIdAndMemberId(commentId, memberId)
			.orElseThrow(() -> new CustomException(GeneralErrorCode.FORBIDDEN_403));

		comment.updateContent(request.getContent());

		return commentConverter.toResponse(comment);
	}

	// 댓글 삭제
	public void deleteComment(Long commentId, Long memberId) {
		Comment comment = commentRepository.findByIdAndMemberId(commentId, memberId)
			.orElseThrow(() -> new CustomException(GeneralErrorCode.FORBIDDEN_403));

		commentRepository.delete(comment);
	}

	// 댓글 조회 (커서 기반 페이지네이션)
	public List<CommentResponseDto> getComments(Long postId, Long cursor, int size) {
		Slice<Comment> comments = commentRepository.findByPostIdAndIdLessThanOrderByIdDesc(
			postId, cursor == null ? Long.MAX_VALUE : cursor, PageRequest.of(0, size));

		return commentConverter.toResponseList(comments.getContent());
	}
}
