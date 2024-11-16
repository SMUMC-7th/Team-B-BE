package com.example.teamB.global.jwt.handler;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 발생하는 AuthenticationException을 처리하여,
// 401 상태 코드와 JSON 형식의 에러 응답을 클라이언트에 반환하는 역할
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8"); // 응답을 JSON 형식으로 설정
        response.setStatus(401); // HTTP 상태 코드를 401 (Unauthorized)로 설정

        // CustomResponse 객체를 생성하여 에러 응답 데이터 구성
        CustomResponse<Object> errorResponse = CustomResponse.onFailure(
                GeneralErrorCode.UNAUTHORIZED_401.getStatus(), // 401 상태 코드 설정
                GeneralErrorCode.UNAUTHORIZED_401.getCode(), // 에러 코드 설정
                GeneralErrorCode.UNAUTHORIZED_401.getMessage(), // 에러 메시지 설정
                false, // 성공 여부를 false로 설정
                null // 추가 데이터 없음
        );

        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper를 사용해 객체를 JSON으로 변환
        mapper.writeValue(response.getOutputStream(), errorResponse); // 에러 응답을 JSON 형태로 출력 스트림에 작성

    }
}