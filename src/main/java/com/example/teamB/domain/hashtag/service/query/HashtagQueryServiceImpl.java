package com.example.teamB.domain.hashtag.service.query;

import com.example.teamB.domain.hashtag.converter.HashtagConverter;
import com.example.teamB.domain.hashtag.dto.HashtagResponseDTO;
import com.example.teamB.domain.hashtag.entity.Hashtag;
import com.example.teamB.domain.hashtag.enums.HashtagCategory;
import com.example.teamB.domain.hashtag.repository.HashtagRepository;
import com.example.teamB.domain.ootd.provider.WeatherClassificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagQueryServiceImpl implements HashtagQueryService {

    private final HashtagRepository hashtagRepository;

    @Override
    public HashtagResponseDTO.DailyRecommendationListDTO getRecommendation(int maxTemperature, int minTemperature) {
        WeatherClassificationProvider weatherClassification
                = WeatherClassificationProvider.getWeatherClassification(minTemperature, maxTemperature);
        List<HashtagCategory> hashtagCategories = weatherClassification.provideRandomHashtags();

        List<Hashtag> hashtags = hashtagRepository.findByCategoryIn(hashtagCategories);
        return HashtagConverter.toDailyRecommendationListDTO(hashtags);
    }
}
