package com.example.teamB.domain.hashtag.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class HashtagException extends CustomException {
    public HashtagException(HashtagErrorCode code) {
        super(code);
    }
}
