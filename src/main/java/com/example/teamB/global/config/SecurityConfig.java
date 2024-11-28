package com.example.teamB.global.config;


import com.example.teamB.domain.member.principal.PrincipalDetailsService;
import com.example.teamB.global.jwt.filter.JwtFilter;
import com.example.teamB.global.jwt.handler.JwtAccessDeniedHandler;
import com.example.teamB.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.example.teamB.global.jwt.util.JwtProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring에서 설정 클래스를 정의할 때 사용되는 애너테이션으로, 해당 클래스가 하나 이상의 @Bean 메서드를 포함하고 있음을 의미 !
// @Configuration 클래스 내에 정의된 @Bean 애너테이션 메서드는 Spring이 관리하는 빈 객체를 반환
// 이를 통해 빈의 생명주기를 Spring 컨테이너가 관리하고, 애플리케이션에서 쉽게 주입하여 사용할 수 있게 됨
// @Configuration 클래스는 @ComponentScan 또는 @SpringBootApplication과 함께 사용되어 Spring이 애플리케이션의 빈을 검색하고 등록할 수 있도록 함
// @ComponentScan이 있는 클래스에서 @Configuration을 사용하여 다른 컴포넌트를 찾아 등록할 수 있음
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 인증을 허용할 URL 배열
    private final String[] allowUrl = {
            "/", // 루트 경로
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/users/signup/request-and-verify",
            "/api/users/signup/verify-code",
            "/api/users/signup",
            "/api/users/login",
            "/api/users/token/reissue",
            "/oauth2/callback/**"
    };

    @Bean // Spring Security의 필터 체인을 설정하는 빈으로 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인증 및 권한 설정
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll() // 지정된 URL은 접근 허용
                        .anyRequest().authenticated()) // 그 외의 모든 요청은 인증 요구

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가하여 인증 절차 진행
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)

                // 기본 폼 로그인 기능을 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 기본 HTTP Basic 인증을 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
                // CSRF 보안을 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 예외 처리 설정: 접근 거부 시 및 인증 실패 시 핸들러 지정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 접근 거부 시 처리할 핸들러 : jwtAccessDeniedHandler
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        // 인증 실패 시 처리할 핸들러 : jwtAuthenticationEntryPoint
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        return http.build(); // 이렇게 필터 체인 생성해서 반환!
    }

    @Bean // JWTFilter를 빈으로 등록
    // JWT 필터에 jwtProvider와 principalDetailsService 주입
    public Filter jwtFilter() {
        return new JwtFilter(jwtProvider, principalDetailsService);
    }

    @Bean // 비밀번호 암호화를 위한 PasswordEncoder 를 빈으로 등록
    // BCryptPasswordEncoder를 사용하여 비밀번호 암호화
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}