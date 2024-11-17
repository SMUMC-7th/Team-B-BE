package com.example.teamB.domain.ootd.service.query;

import com.example.teamB.domain.ootd.converter.OotdConverter;
import com.example.teamB.domain.ootd.dto.OotdResponseDTO;
import com.example.teamB.domain.ootd.entity.Ootd;
import com.example.teamB.domain.ootd.provider.WeatherClassificationProvider;
import com.example.teamB.domain.ootd.repository.OotdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OotdQueryServiceImpl implements OotdQueryService {

    private final OotdRepository ootdRepository;

    @Override
    public OotdResponseDTO.OotdInfoListDTO getMonthlyOotd(int year, int month, long memberId) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Ootd> ootdList = ootdRepository.findByMemberIdAndCreatedAtBetween(memberId, startOfMonth, endOfMonth);
        return OotdConverter.toOotdInfoListDTO(ootdList);
    }

    @Override
    public OotdResponseDTO.OotdInfoListDTO getPastOotd(int maxTemperature, int minTemperature, long memberId) {
        WeatherClassificationProvider weatherClassification
                = WeatherClassificationProvider.getWeatherClassification(minTemperature, maxTemperature);

        List<Ootd> ootdList = ootdRepository.findByMemberIdAndTemperatureRange(
                memberId,
                weatherClassification.getMinTemperature(),
                weatherClassification.getMaxTemperature());

        // ootd양이 많아지면 shuffle보다는 random 숫자를 뽑아서 접근하는게 빠를 수 있음
        Collections.shuffle(ootdList);
        List<Ootd> randomOotdList = ootdList.stream().limit(Math.min(ootdList.size(), 2)).toList();
        return OotdConverter.toOotdInfoListDTO(randomOotdList);
    }

}
