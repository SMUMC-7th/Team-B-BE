package com.example.teamB.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostRequestDTO {

    @Getter
    public static class CreatePostDTO {
        private String title;
        private String content;
    }

    @Getter
    public static class UpdatePostDTO {
        private String title;
        private String content;
    }

}
