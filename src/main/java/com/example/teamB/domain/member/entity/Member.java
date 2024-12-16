package com.example.teamB.domain.member.entity;

import com.example.teamB.domain.member.enums.Gender;
import com.example.teamB.domain.member.enums.MemberStatus;
import com.example.teamB.domain.ootd.entity.Ootd;
import com.example.teamB.global.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(nullable = false, length = 50)
    public String email;

    @Column(length = 100)
    public String password; // 비밀번호는 nullable로 설정 (소셜 로그인 시 필요 없을 수 있음)

    @Column(length = 20)
    public String name;

    @Column(length = 20)
    public String nickname;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    public Gender gender;

    @Column(name = "alarm_status")
    public Boolean alarmStatus;

    @Column(name = "alarm_time", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime alarmTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    public MemberStatus status;

    @Column(name = "inactive_date")
    public LocalDateTime inactiveDate;

    // 카카오 관련 필드
    @Column(name = "kakao_id", unique = true)
    private String kakaoId; // 카카오 계정 고유 ID

    @Column(name = "profile_image_url")
    private String profileImageUrl; // 프로필 이미지 URL

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ootd> ootds = new ArrayList<>();

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setAlarmStatus(Boolean alarmStatus) { this.alarmStatus = alarmStatus; }
    public void setAlarmTime(LocalTime alarmTime) { this.alarmTime = alarmTime; }
    public void setPassword(String password) {
        this.password = password;
    }
}
