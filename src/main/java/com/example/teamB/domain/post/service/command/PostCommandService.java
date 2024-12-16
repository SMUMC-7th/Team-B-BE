package com.example.teamB.domain.post.service.command;

import com.example.teamB.domain.post.dto.PostRequestDTO;

public interface PostCommandService {
    Long createPost(PostRequestDTO.CreatePostDTO dto, Long memberId);
    void updatePost(PostRequestDTO.CreatePostDTO dto, Long memberId, Long postId);
    void deletePost(Long postId, Long memberId);
}
