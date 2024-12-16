package com.example.teamB.domain.comment.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class CommentException extends CustomException {
    public CommentException(CommentErrorCode code) {
        super(code);
    }
}
