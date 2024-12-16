package com.example.teamB.domain.comment.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Lob
	private String content;

	@Column(name = "report_count")
	private int reportCount = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Comment parent;

	@OneToMany(mappedBy = "parent")
	private List<Comment> children = new ArrayList<>();

	public Comment(String content, Member member, Post post, Comment parent) {
		this.content = content;
		this.member = member;
		this.post = post;
		this.parent = parent;
	}

	public void updateContent(@NotBlank(message = "수정된 댓글 내용은 필수입니다.") String content) {
		this.content = content;
	}
}
