package com.example.teamB.domain.hashtag.dto;

import lombok.*;

import java.util.List;

public class HashtagResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DailyRecommendationDTO {
        private String hashtag;
        private String image;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DailyRecommendationListDTO {
        private List<DailyRecommendationDTO> recommendations;
    }
}
