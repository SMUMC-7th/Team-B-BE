package com.example.teamB.domain.comment.service.query;

import com.example.teamB.domain.comment.dto.CommentResponseDto;

public interface CommentQueryService {
    CommentResponseDto.CommentPreviewListDTO getComments(Long postId, Long cursorId, int size);
}
