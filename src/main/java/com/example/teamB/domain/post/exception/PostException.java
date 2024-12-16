package com.example.teamB.domain.post.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class PostException extends CustomException {
    public PostException(PostErrorCode code) {
        super(code);
    }
}
