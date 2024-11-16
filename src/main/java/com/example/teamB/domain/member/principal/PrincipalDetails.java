package com.example.teamB.domain.member.principal;


import com.example.teamB.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Spring Security에서 인증된 사용자 정보를 담는 클래스
 * UserDetails 인터페이스를 구현하여 사용자의 인증과 권한 정보를 제공
 * 주로 로그인 이후 인증된 사용자 정보를 조회하거나 권한 검사를 할 때 사용
 */
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Member member;


    /** 현재 사용자의 권한을 반환하는 method *
     * member.getRole()에서 가져온 역할(권한)을 SimpleGrantedAuthority로 변환하여
     * Spring Security에서 인식할 수 있는 형태로 반환함

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>(); // 사용자 roles 리스트 생성
        roles.add(member.getRole()); // roles 리스트에 role add
        // SimpleGrantedAuthority 객체 리스트로 변환하여 반환
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }*/
    // 이번 프로젝트에서는 jwt로 role을 안나누고 진행해서 위 함수를 implement만 되도록 구현해둠
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 사용자 이름을 반환하는 메서드, 여기서는 이메일을 사용자 이름으로 사용
    @Override
    public String getUsername() {
        return member.getEmail();
    }


    // 사용자 비밀번호를 반환하는 메서드
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 계정 활성화 상태를 반환, true면 활성화됨
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정의 만료 여부를 반환, true면 만료되지 않음
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정의 잠김 여부를 반환, true면 잠기지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 자격 증명(비밀번호 등)의 만료 여부를 반환, true면 만료되지 않음
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}