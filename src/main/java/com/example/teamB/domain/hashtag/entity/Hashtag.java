package com.example.teamB.domain.hashtag.entity;

import java.util.List;

import com.example.teamB.domain.hashtag.enums.HashtagCategory;
import com.example.teamB.domain.ootdHashtag.entity.OotdHashtag;
import com.example.teamB.global.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hashtag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", columnDefinition = "VARCHAR(20)")
    private HashtagCategory category;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OotdHashtag> ootdHashtags;
}