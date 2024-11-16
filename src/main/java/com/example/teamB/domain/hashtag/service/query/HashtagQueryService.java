package com.example.teamB.domain.hashtag.service.query;

import com.example.teamB.domain.hashtag.dto.HashtagResponseDTO;

public interface HashtagQueryService {
    HashtagResponseDTO.DailyRecommendationListDTO getRecommendation(int maxTemperature, int minTemperature);
}
