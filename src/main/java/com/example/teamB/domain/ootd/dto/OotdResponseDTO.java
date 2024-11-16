package com.example.teamB.domain.ootd.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class OotdResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OotdInfoDTO {
        private String image;
        private Integer minTemperature;
        private Integer maxTemperature;
        private String weatherDescription;
        private List<String> hashtags;
        private LocalDate date;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OotdInfoListDTO {
        private List<OotdInfoDTO> ootds;
    }


}
