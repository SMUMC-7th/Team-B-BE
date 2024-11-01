package com.example.teamB.global.apiPayload.exception;

import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private BaseErrorCode code;

    public CustomException(BaseErrorCode code) {
        this.code = code;
    }

}
