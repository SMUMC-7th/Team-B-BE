package com.example.teamB.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.exception.CustomException;
import com.example.teamB.global.apiPayload.code.GeneralErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Tag(name="테스트용 API")
public class TestController {
    @GetMapping("/test")
    @Operation(summary="테스트하는 API", description="테스트용도이며 Hello World를 반환합니다.")
    public CustomResponse<String> test() {
        return CustomResponse.onSuccess("Hello World");
    }

    @GetMapping("/test/failure")
    public CustomResponse<String> failure(@RequestParam int exception) {
        if (exception == 0) {
            throw new CustomException(GeneralErrorCode.BAD_REQUEST_400);
        }
        else if (exception == 1) {
            int a = 1 / 0;
        }
        return CustomResponse.onSuccess("에러 핸들러 동작하지 않음");
    }
}