package com.example.teamB.domain.comment.converter;

import com.example.teamB.domain.comment.dto.CommentResponseDTO;
import org.springframework.stereotype.Component;

import com.example.teamB.domain.comment.entity.Comment;

import java.util.List;

/**
 * Comment 엔티티와 DTO 간 변환기
 */
@Component
public class CommentConverter {

    public CommentResponseDTO.CommentPreviewDTO toCommentPreviewDTO(Comment comment, List<CommentResponseDTO.CommentPreviewDTO> childResponses) {
        return CommentResponseDTO.CommentPreviewDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .children(childResponses)
                .build();
    }

    public CommentResponseDTO.CommentPreviewListDTO toCommentPreviewListDTO(List<CommentResponseDTO.CommentPreviewDTO> childResponses) {
        return CommentResponseDTO.CommentPreviewListDTO.builder()
                .list(childResponses)
                .lastId(childResponses.isEmpty() ? 0L : childResponses.get(childResponses.size() - 1).getId())
                .build();
    }


}