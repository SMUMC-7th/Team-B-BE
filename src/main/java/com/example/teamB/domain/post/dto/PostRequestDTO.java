package com.example.teamB.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


public class PostRequestDTO {

    @Getter
    public static class CreatePostDTO {
        @Schema(description = "게시물 제목")
        @NotNull
        private String title;

        @Schema(description = "게시물 내용")
        @NotNull
        private String content;
    }

}
