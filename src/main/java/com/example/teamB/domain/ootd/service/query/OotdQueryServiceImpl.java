package com.example.teamB.domain.ootd.service.query;

import com.example.teamB.domain.ootd.converter.OotdConverter;
import com.example.teamB.domain.ootd.dto.OotdResponseDTO;
import com.example.teamB.domain.ootd.entity.Ootd;
import com.example.teamB.domain.ootd.repository.OotdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        List<Ootd> ootdList = ootdRepository.findByMemberIdAndCreatedAtBetween(1L, startOfMonth, endOfMonth);
        return OotdConverter.toOotdInfoListDTO(ootdList);
    }
}
