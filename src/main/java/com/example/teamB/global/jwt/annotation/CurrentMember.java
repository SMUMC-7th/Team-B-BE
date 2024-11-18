package com.example.teamB.global.jwt.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AuthenticationPrincipal
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentMember {
}
