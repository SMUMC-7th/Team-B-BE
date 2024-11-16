package com.example.teamB.domain.ootd.converter;

import com.example.teamB.domain.ootd.dto.OotdResponseDTO;
import com.example.teamB.domain.ootd.entity.Ootd;

import java.util.List;
import java.util.stream.Collectors;

public class OotdConverter {

    public static OotdResponseDTO.OotdInfoDTO toOotdInfoDTO(Ootd ootd) {
        List<String> hashtagNames = ootd.getOotdHashtags().stream()
                .map(ootdHashtag -> ootdHashtag.getHashtag().getCategory().getKoreanName())
                .collect(Collectors.toList());

        return OotdResponseDTO.OotdInfoDTO.builder()
                .image(ootd.getImage())
                .minTemperature(ootd.getMinTemperature())
                .maxTemperature(ootd.getMaxTemperature())
                .weatherDescription(ootd.getWeatherDescription())
                .hashtags(hashtagNames)
                .date(ootd.getCreatedAt().toLocalDate())
                .build();
    }

    public static OotdResponseDTO.OotdInfoListDTO toOotdInfoListDTO(List<Ootd> ootdList) {
        List<OotdResponseDTO.OotdInfoDTO> ootdInfoDTOs = ootdList.stream()
                .map(OotdConverter::toOotdInfoDTO)
                .collect(Collectors.toList());

        return OotdResponseDTO.OotdInfoListDTO.builder()
                .ootds(ootdInfoDTOs)
                .build();
    }
}
