package com.example.teamB.domain.weather.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.teamB.domain.ootd.entity.Ootd;
import com.example.teamB.domain.region.entity.Region;
import com.example.teamB.global.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Weather extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(name = "weather_description", length = 30, nullable = false)
    private String weatherDescription;

    @Column(name = "min_temperature", nullable = false)
    private int minTemperature;

    @Column(name = "max_temperature", nullable = false)
    private int maxTemperature;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ootd> ootds;
}