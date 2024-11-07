package com.example.teamB.domain.ootd.entity;

import java.util.List;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.ootdHashtag.entity.OotdHashtag;
import com.example.teamB.domain.weather.entity.Weather;
import com.example.teamB.global.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ootd")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Ootd extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @Column(name = "image", columnDefinition = "TEXT", nullable = false)
    private String image;

    @OneToMany(mappedBy = "ootd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OotdHashtag> ootdHashtags;
}