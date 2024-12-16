package com.example.teamB.domain.comment.service.command;


import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentResponseDTO;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.domain.member.entity.Member;

public interface CommentCommandService {
    CommentResponseDTO.CommentPreviewDTO createComment(CommentCreateRequestDto request, Member member, Long postId);
    CommentResponseDTO.CommentPreviewDTO updateComment(Long commentId, CommentUpdateRequestDto request, Long memberId);
    void deleteComment(Long commentId, Long memberId);
    void reportComment(Long commentId, Long memberId);
}
