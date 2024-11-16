package com.example.teamB.domain.ootd.controller;

import com.example.teamB.domain.ootd.dto.OotdRequestDTO;
import com.example.teamB.domain.ootd.dto.OotdResponseDTO;
import com.example.teamB.domain.ootd.service.command.OotdCommandService;
import com.example.teamB.domain.ootd.service.query.OotdQueryService;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ootds")
@Tag(name = "OOTD API")
public class OotdController {

    private final OotdCommandService ootdCommandService;
    private final OotdQueryService ootdQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "오늘의 OOTD 기록 등록", description = "사용자가 오늘의 OOTD 기록 등록, swagger로는 테스트가 불가 합니다. postman을 활용해 주세요!")
    public CustomResponse<?> createOotd(@RequestPart MultipartFile image,
                                        @RequestPart OotdRequestDTO.CreateOotdDTO dto) {
        ootdCommandService.createOotd(dto, image, 1L);
        return CustomResponse.onSuccess(null);
    }

    @GetMapping
    @Operation(summary = "월별 등록 ootd 조회", description = "월별로 등록된 ootd를 조회합니다.")
    public CustomResponse<OotdResponseDTO.OotdInfoListDTO> getMonthlyOotd(@RequestParam int year, @RequestParam int month) {
        return CustomResponse.onSuccess(ootdQueryService.getMonthlyOotd(year, month, 1L));
    }

    @GetMapping("/past")
    @Operation(summary = "과거 OOTD 기록 조회", description = "비슷한 기온에 과거에 등록한 OOTD를 조회합니다.")
    public CustomResponse<OotdResponseDTO.OotdInfoListDTO> getPastOotd(
            @RequestParam(value = "maxTemperature") int maxTemperature,
            @RequestParam(value = "minTemperature") int minTemperature) {
        return CustomResponse.onSuccess(ootdQueryService.getPastOotd(maxTemperature, minTemperature, 1L));
    }

}
