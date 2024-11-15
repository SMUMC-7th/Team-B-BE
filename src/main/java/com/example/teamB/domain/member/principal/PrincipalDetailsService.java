package com.example.teamB.domain.member.principal;

import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security에서 사용자 정보를 조회하기 위해 UserDetailsService 인터페이스를 구현한 서비스
 * 사용자의 이메일을 기반으로 데이터를 조회하고, 인증에 사용할 UserDetails 객체를 반환
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 주어진 username(이메일)으로 사용자를 조회하여 UserDetails를 반환하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일로 회원 정보 조회
        Member member = memberRepository.findByEmail(username).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND)); // 회원이 없을 경우 예외 발생
        // 조회된 회원 정보를 기반으로 PrincipalDetails 객체를 생성해 반환
        return new PrincipalDetails(member);
    }
}