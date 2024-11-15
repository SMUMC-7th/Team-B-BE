package com.example.teamB.domain.member.exception;

import com.example.teamB.global.apiPayload.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(MemberErrorCode code) {
        super(code);
    }
}