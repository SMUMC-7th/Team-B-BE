package com.example.teamB.domain.member.annotation.resolver;

import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.service.query.MemberQueryService;
import com.example.teamB.global.jwt.exception.JwtErrorCode;
import com.example.teamB.global.jwt.util.JwtProvider;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedMemberResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final MemberQueryService memberQueryService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class) && parameter.getParameterType().isAssignableFrom(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String header = webRequest.getHeader("Authorization");

        if (header != null) {
            String token = header.split(" ")[1];
            String email = jwtProvider.getEmail(token);
            return memberQueryService.getMember(email);
        }

        return null;
    }
}