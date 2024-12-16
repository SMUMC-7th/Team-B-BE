package com.example.teamB.domain.post.exception;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostErrorCode implements BaseErrorCode {

    POST_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST,
            "POST001",
            "게시물 등록에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public <T> CustomResponse<T> getResponse() {
        return CustomResponse.onFailure(this.status, this.code, this.message, false, null);
    }
}
