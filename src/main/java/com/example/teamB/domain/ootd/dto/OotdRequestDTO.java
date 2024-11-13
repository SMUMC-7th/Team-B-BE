package com.example.teamB.domain.ootd.dto;

import lombok.Getter;

import java.util.List;

public class OotdRequestDTO {
    @Getter
    public static class CreateOotdDTO {
        private Integer maxTemperature;
        private Integer minTemperature;
        private List<String> hashtags;
    }
}
