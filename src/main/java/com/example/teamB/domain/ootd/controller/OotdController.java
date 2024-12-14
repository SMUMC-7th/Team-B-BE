package com.example.teamB.domain.ootd.controller;

import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.ootd.dto.OotdRequestDTO;
import com.example.teamB.domain.ootd.dto.OotdResponseDTO;
import com.example.teamB.domain.ootd.service.command.OotdCommandService;
import com.example.teamB.domain.ootd.service.query.OotdQueryService;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER404", description = "조회되는 멤버가 없습니다. 토큰을 확인해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UPLOAD001", description = "파일 업로드에 실패했습니다. 등록하려는 파일을 확인해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OOTD002", description = "적절한 날씨 분류를 찾지 못했습니다. 기온 입력정보를 다시 확인해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "HASHTAG001", description = "입력한 해시태그를 찾을 수 없습니다. 해시태그 이름을 확인해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public CustomResponse<?> createOotd(
            @CurrentMember Member member,
            @RequestPart MultipartFile image,
            @RequestPart OotdRequestDTO.CreateOotdDTO dto) {
        ootdCommandService.createOotd(dto, image, member.getId());
        return CustomResponse.onSuccess(null);
    }

    @GetMapping
    @Operation(summary = "월별 등록 ootd 조회", description = "월별로 등록된 ootd를 조회합니다.")
    @Parameters({
            @Parameter(name = "year", description = "ootd를 조회할 년도"),
            @Parameter(name = "month", description = "ootd를 조회할 월")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    public CustomResponse<OotdResponseDTO.OotdInfoListDTO> getMonthlyOotd(
            @CurrentMember Member member,
            @RequestParam int year,
            @RequestParam int month) {
        return CustomResponse.onSuccess(ootdQueryService.getMonthlyOotd(year, month, member.getId()));
    }

    @GetMapping("/past")
    @Operation(summary = "과거 OOTD 기록 조회", description = "비슷한 기온에 과거에 등록한 OOTD를 조회합니다.")
    @Parameters({
            @Parameter(name = "maxTemperature", description = "날씨 Api로 조회한 당일 최고 기온"),
            @Parameter(name = "minTemperature", description = "날씨 Api로 조회한 당일 최저 기온")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OOTD002", description = "적절한 날씨 분류를 찾지 못했습니다. 기온 입력정보를 다시 확인해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public CustomResponse<OotdResponseDTO.OotdInfoListDTO> getPastOotd(
            @CurrentMember Member member,
            @RequestParam(value = "maxTemperature") int maxTemperature,
            @RequestParam(value = "minTemperature") int minTemperature) {
        return CustomResponse.onSuccess(ootdQueryService.getPastOotd(maxTemperature, minTemperature, member.getId()));
    }

}
