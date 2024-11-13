package com.example.teamB.domain.ootd.provider;

import com.example.teamB.domain.hashtag.enums.HashtagCategory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.teamB.domain.hashtag.enums.HashtagCategory.*;

/**
 * 기온 분류, 적절한 옷, 날씨 설명을 저장하고, 반환하는 Enum
 */
public enum WeatherClassificationProvider {

    EXTREMELY_COLD(-50, -10,
            Arrays.asList(PADDED_COAT, COAT, FLEECE, KNIT, CARDIGAN, HOODIE, SWEATSHIRT, LONG_PANTS, SCARF, BEANIE),
            "기온이 매우 낮아 보온이 필수인 매우 추운 날"),

    VERY_COLD(-9, -1,
            Arrays.asList(PADDED_COAT, COAT, FLEECE, KNIT, CARDIGAN, HOODIE, SWEATSHIRT, LONG_PANTS, SCARF, BEANIE),
            "바람이 불면 체감 온도가 떨어질 수 있는 추운 날"),

    COLD(0, 4,
            Arrays.asList(PADDED_COAT, COAT, FLEECE, KNIT, CARDIGAN, HOODIE, SWEATSHIRT, SHIRT, LONG_SLEEVE_TEE, LONG_PANTS, SCARF, BEANIE),
            "외투와 적절한 레이어링이 필요한 쌀쌀한 날"),

    COOL(5, 9,
            Arrays.asList(COAT, FLEECE, HOODIE, JACKET, SWEATSHIRT, KNIT),
            "적당한 두께의 외투가 필요한 날"),

    MILD(10, 14,
            Arrays.asList(CARDIGAN, SHIRT, LONG_SLEEVE_TEE, JACKET, WIND_BREAKER),
            "얇게 입기는 추운 날"),

    PLEASANT(15, 19,
            Arrays.asList(SHIRT, SHORT_SLEEVE_TEE, SWEATSHIRT, KNIT),
            "기온이 딱 적당하여 산책하기 좋은 날"),

    WARM(20, 24,
            Arrays.asList(SHORT_SLEEVE_TEE, SLEEVELESS, SHIRT, SANDALS),
            "기온이 선선해지며 가볍게 입기 좋은 날"),

    HOT(25, 29,
            Arrays.asList(SHORT_SLEEVE_TEE, SHORT_PANTS, SLEEVELESS, SANDALS),
            "더운 날씨로 가벼운 옷차림이 좋은 날"),

    SCORCHING(30, 50,
            Arrays.asList(SLEEVELESS, SHORT_SLEEVE_TEE, SHORT_PANTS, SUNGLASSES, SANDALS),
            "기온이 매우 높은 무더운 날");

    private final int minTemperature;
    private final int maxTemperature;
    private final List<HashtagCategory> hashtags;
    private final String description;

    WeatherClassificationProvider(int minTemperature, int maxTemperature, List<HashtagCategory> hashtags, String description) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.hashtags = hashtags;
        this.description = description;
    }

    public boolean supports(int temperature) {
        return temperature >= minTemperature && temperature <= maxTemperature;
    }

    public List<HashtagCategory> provideRandomHashtags() {
        Collections.shuffle(hashtags);
        return hashtags.subList(0, Math.min(3, hashtags.size()));
    }

    public String getWeatherDescription() {
        return description;
    }
}
