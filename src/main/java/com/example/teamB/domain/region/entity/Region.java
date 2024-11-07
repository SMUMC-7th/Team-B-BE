package com.example.teamB.domain.region.entity;

import java.util.List;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.region.enums.RegionName;
import com.example.teamB.domain.weather.entity.Weather;
import com.example.teamB.global.common.BaseEntity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "region")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", columnDefinition = "VARCHAR(15)", nullable = false)
    private RegionName name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Weather> weathers;
}