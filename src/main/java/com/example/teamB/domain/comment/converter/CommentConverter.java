package com.example.teamB.domain.comment.converter;

import com.example.teamB.domain.comment.dto.CommentResponseDto;
import org.springframework.stereotype.Component;

import com.example.teamB.domain.comment.entity.Comment;

import java.util.List;

/**
 * Comment 엔티티와 DTO 간 변환기
 */
@Component
public class CommentConverter {

    public CommentResponseDto.CommentPreviewDTO toCommentPreviewDTO(Comment comment, List<CommentResponseDto.CommentPreviewDTO> childResponses) {
        return CommentResponseDto.CommentPreviewDTO.builder()
                .id(comment.getId())
                .content(comment.getContent()) // 내용은 항상 반환
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .reportCount(comment.getReportCount())
                .createdAt(comment.getCreatedAt())
                .isHidden(comment.isHidden()) // 숨김 여부를 프런트에서 판단해서 해당 내용을 가림
                .children(childResponses)
                .build();
    }

    public CommentResponseDto.CommentPreviewListDTO toCommentPreviewListDTO(List<CommentResponseDto.CommentPreviewDTO> childResponses) {
        return CommentResponseDto.CommentPreviewListDTO.builder()
                .list(childResponses)
                .lastId(childResponses.isEmpty() ? 0L : childResponses.get(childResponses.size() - 1).getId())
                .build();
    }
}