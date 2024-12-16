package com.example.teamB.domain.comment.service.command;


import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentResponseDto;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.domain.member.entity.Member;

public interface CommentCommandService {
    CommentResponseDto.CommentPreviewDTO createComment(CommentCreateRequestDto request, Member member, Long postId);
    CommentResponseDto.CommentPreviewDTO updateComment(Long commentId, CommentUpdateRequestDto request, Long memberId);
    void deleteComment(Long commentId, Long memberId);
    void reportComment(Long commentId, Long memberId);
}
