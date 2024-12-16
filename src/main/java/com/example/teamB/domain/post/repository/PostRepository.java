package com.example.teamB.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.teamB.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
