package com.example.teamB.domain.ootd.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class OotdException extends CustomException {
    public OotdException(OotdErrorCode code) {
        super(code);
    }
}
