package com.example.teamB.domain.comment.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.teamB.domain.comment.dto.CommentResponseDto;
import com.example.teamB.domain.comment.entity.Comment;

/**
 * Comment 엔티티와 DTO 간 변환기
 */
@Component
public class CommentConverter {

    public CommentResponseDto toResponse(Comment comment) {
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> toResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}