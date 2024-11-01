package com.example.teamB.global.apiPayload.code;

import com.example.teamB.global.apiPayload.CustomResponse;
import org.springframework.http.HttpStatus;
public interface BaseErrorCode {
    <T> CustomResponse<T> getResponse();
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
