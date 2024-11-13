package com.example.teamB.domain.ootd.controller;

import com.example.teamB.domain.ootd.dto.OotdRequestDTO;
import com.example.teamB.domain.ootd.service.command.OotdCommandService;
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
@RequestMapping("/api/ootd")
@Tag(name = "OOTD API")
public class OotdController {

    private final OotdCommandService ootdCommandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "오늘의 OOTD 기록 등록", description = "사용자가 오늘의 OOTD 기록 등록, swagger로는 테스트가 불가 합니다. postman을 활용해 주세요!")
    public CustomResponse<?> createOotd(@RequestPart MultipartFile image,
                                        @RequestPart OotdRequestDTO.CreateOotdDTO dto) {
        ootdCommandService.createOotd(dto, image, 1L);
        return CustomResponse.onSuccess(null);
    }

}
