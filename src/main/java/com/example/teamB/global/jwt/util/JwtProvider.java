package com.example.teamB.global.jwt.util;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.global.jwt.exception.AuthException;
import com.example.teamB.global.jwt.exception.JwtErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j // 로그를 사용하기 위헤
@Component
public class JwtProvider {

    // JWT 서명을 위한 비밀 키
    private SecretKey secret;

    // Access 토큰의 만료 시간
    private long accessExpiration;

    // Refresh 토큰의 만료 시간
    private long refreshExpiration;

    // application.yml 에서 값을 가져와 초기화
    public JwtProvider(@Value("${Jwt.secret}") String secret, @Value("${Jwt.token.access-expiration-time}") long accessExpiration, @Value("${Jwt.token.refresh-expiration-time}") long refreshExpiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 비밀 키를 설정
        this.accessExpiration = accessExpiration; // Access 토큰 만료 시간 설정
        this.refreshExpiration = refreshExpiration; // Refresh 토큰 만료 시간 설정
    }

    // Access 토큰을 생성하는 메서드
    public String createAccessToken(Member member) {
        // Access 토큰 생성 메서드 호출
        return createToken(member, this.accessExpiration);
    }

    // Refresh 토큰을 생성하는 메서드
    public String createRefreshToken(Member member) {
        // Refresh 토큰 생성 메서드 호출
        return createToken(member, this.refreshExpiration);
    }

    // 공통 토큰 생성 메서드, 만료 시간을 설정하여 토큰 생성
    public String createToken(Member member, long expiration) {
        // 현재 시간을 발행 시간으로 설정
        Instant issuedAt = Instant.now();
        // 만료 시간 계산
        Instant expiredAt = issuedAt.plusMillis(expiration);
        // JWT 생성 빌더 시작
        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "JWT")) // 헤더 설정
                .setSubject(member.getEmail()) // 토큰의 주제(subject)로 사용자 이메일 설정
                .claim("id", member.getId()) // 사용자 ID를 클레임으로 추가
                .setIssuedAt(Date.from(issuedAt)) // 발행 시간 설정
                .setExpiration(Date.from(expiredAt)) // 만료 시간 설정
                .signWith(secret, SignatureAlgorithm.HS256) // 서명 알고리즘과 비밀 키 설정
                .compact(); // 토큰 생성 및 반환
    }

    // 토큰이 유효한지 확인하는 메서드
    public boolean isValid(String token) {
        try {
            // 토큰의 클레임을 가져옴
            Jws<Claims> claims = getClaims(token);
            // 만료 시간을 확인하여 유효성 검사
            return claims.getBody().getExpiration().after(Date.from(Instant.now()));
        } catch (JwtException e) { // JWT 관련 예외 처리
            log.error(e.getMessage()); // 예외 메시지를 로그에 기록
            return false;
        } catch (Exception e) { // 일반 예외 처리
            log.error(e.getMessage() + ": 토큰이 유효하지 않습니다."); // 예외 메시지를 로그에 기록
            return false;
        }
    }

    // 토큰의 클레임을 가져오는 메서드
    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder() // JWT 파서 빌더 시작
                    .setSigningKey(secret) // sign key, 서명 키 설정
                    .build()
                    .parseClaimsJws(token); // 토큰을 파싱하여 클레임 반환
        } catch (Exception e) { // parsing하는 과정에서 sign key가 틀리는 등의 이유로 일어나는 Exception
            throw new AuthException(JwtErrorCode.INVALID_TOKEN); // 커스텀 예외를 발생시킴
        }
    }

    // 토큰에서 이메일 정보를 추출하는 메서드
    public String getEmail(String token) {
        // 토큰의 주제(subject)에서 이메일 반환
        return getClaims(token).getBody().getSubject();
    }

}