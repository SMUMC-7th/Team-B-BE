package com.example.teamB.domain.comment.exception;

import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT001", "댓글를 찾을 수 없습니다."),
    REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT002", "댓글을 신고할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public <T> CustomResponse<T> getResponse() {
        return CustomResponse.onFailure(this.status, this.code, this.message, false, null);
    }
}
