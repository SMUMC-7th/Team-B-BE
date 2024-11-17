package com.example.teamB.domain.member.exception;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    // 회원 관련 에러
    NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "사용자를 찾을 수 없습니다."),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER400", "이미 존재하는 사용자입니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER401", "비밀번호가 틀립니다."),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "MEMBER403", "비활성화된 계정입니다."),

    // 인증 관련 에러
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL500", "Mail 전송에 실패했습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "MEMBER400", "유효하지 않은 인증코드입니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "MEMBER400", "인증코드가 만료되었습니다."),
    UNVERIFIED_EMAIL(HttpStatus.FORBIDDEN, "MEMBER403", "인증되지 않은 이메일입니다."),

    // JWT 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "유효하지 않은 토큰입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public <T> CustomResponse<T> getResponse() {
        return null;
    }
}