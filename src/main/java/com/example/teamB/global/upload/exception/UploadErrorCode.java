package com.example.teamB.global.upload.exception;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UploadErrorCode implements BaseErrorCode {

    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,
            "UPLOAD001",
            "업로드 처리 중 오류가 발생했습니다."),
    EXT_ERROR(HttpStatus.BAD_REQUEST,
            "UPLOAD002",
            "업로드 파일 확장자가 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public <T> CustomResponse<T> getResponse() {
        return CustomResponse.onFailure(this.status, this.code, this.message, false, null);
    }
}
