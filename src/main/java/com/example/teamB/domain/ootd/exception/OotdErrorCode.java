package com.example.teamB.domain.ootd.exception;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OotdErrorCode implements BaseErrorCode {

    OOTD_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST,
            "OOTD001",
            "OOTD 등록에 실패하였습니다."),
    WEATHER_SEARCH_FAILED(HttpStatus.BAD_REQUEST,
            "OOTD002",
            "날씨 분류 검색에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public <T> CustomResponse<T> getResponse() {
        return CustomResponse.onFailure(this.status, this.code, this.message, false, null);
    }
}
