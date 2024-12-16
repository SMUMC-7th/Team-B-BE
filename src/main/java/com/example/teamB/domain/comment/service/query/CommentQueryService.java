package com.example.teamB.domain.comment.service.query;

import com.example.teamB.domain.comment.dto.CommentResponseDTO;

public interface CommentQueryService {
    CommentResponseDTO.CommentPreviewListDTO getComments(Long postId, Long cursorId, int size);
}
