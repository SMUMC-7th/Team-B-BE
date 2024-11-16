package com.example.teamB.domain.hashtag.converter;

import com.example.teamB.domain.hashtag.dto.HashtagResponseDTO;
import com.example.teamB.domain.hashtag.entity.Hashtag;

import java.util.List;

public class HashtagConverter {

    public static HashtagResponseDTO.DailyRecommendationDTO toDailyRecommendationDTO(Hashtag hashtag) {
        return HashtagResponseDTO.DailyRecommendationDTO.builder()
                .image(hashtag.getImage())
                .hashtag(hashtag.getCategory().getKoreanName())
                .build();
    }

    public static HashtagResponseDTO.DailyRecommendationListDTO toDailyRecommendationListDTO(List<Hashtag> hashtags) {
        return HashtagResponseDTO.DailyRecommendationListDTO.builder()
                .recommendations(hashtags.stream().map(HashtagConverter::toDailyRecommendationDTO).toList())
                .build();
    }
}
