package com.example.teamB.global.jwt.handler;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 접근 권한이 없을 때 발생하는 AccessDeniedException을 처리하여,
// 403 상태 코드와 JSON 형식의 에러 응답을 클라이언트에 반환하는 핸들러
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 응답 타입을 JSON으로 설정
        response.setContentType("application/json; charset=UTF-8");
        // HTTP 상태 코드를 403 (Forbidden)으로 설정
        response.setStatus(403);

        // CustomResponse 객체를 생성하여 에러 응답 데이터 구성
        CustomResponse<Object> errorResponse = CustomResponse.onFailure(
                GeneralErrorCode.FORBIDDEN_403.getStatus(), // 403 상태 코드 설정
                GeneralErrorCode.FORBIDDEN_403.getCode(), // 에러 코드 설정
                GeneralErrorCode.FORBIDDEN_403.getMessage(), // 에러 메시지 설정
                false, // 성공 여부를 false로 설정
                null // 추가 데이터 없음
        );

        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper를 이용해 객체를 JSON으로 변환
        mapper.writeValue(response.getOutputStream(), errorResponse); // 에러 응답을 JSON 형태로 출력 스트림에 작성
    }
}