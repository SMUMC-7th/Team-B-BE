package com.example.teamB.domain.ootd.service.query;

import com.example.teamB.domain.ootd.dto.OotdResponseDTO;

public interface OotdQueryService {
    OotdResponseDTO.OotdInfoListDTO getMonthlyOotd(int year, int month, long memberId);
    OotdResponseDTO.OotdInfoListDTO getPastOotd(int maxTemperature, int minTemperature, long memberId);
}
