package com.example.teamB.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentPreviewDTO {
        private Long id;
        private String content;
        private Long parentId;
        private Long memberId;
        private String memberNickname;
        private int reportCount;
        private LocalDateTime createdAt;
        private boolean isHidden; // 댓글 숨김여부
        private List<CommentPreviewDTO> children;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentPreviewListDTO {
        private List<CommentPreviewDTO> list;
        private Long lastId;
    }
}
