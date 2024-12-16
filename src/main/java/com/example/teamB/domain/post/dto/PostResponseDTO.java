package com.example.teamB.domain.post.dto;


import lombok.*;

import java.util.List;

public class PostResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreViewListDTO {
        List<PostResponseDTO.PostPreViewDTO> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreViewDTO {
        String memberName;
        String title;
        String content;
    }
}
