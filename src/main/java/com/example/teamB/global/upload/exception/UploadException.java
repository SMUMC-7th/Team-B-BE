package com.example.teamB.global.upload.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class UploadException extends CustomException {
    public UploadException(UploadErrorCode code) {
        super(code);
    }
}
