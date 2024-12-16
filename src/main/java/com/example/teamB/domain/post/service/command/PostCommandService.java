package com.example.teamB.domain.post.service.command;

import com.example.teamB.domain.post.dto.PostRequestDTO;
import com.example.teamB.domain.post.dto.PostResponseDTO;
import com.example.teamB.domain.post.entity.Post;

public interface PostCommandService {
    Long createPost(PostRequestDTO.CreatePostDTO dto, Long memberId);
    Post updatePost(PostRequestDTO.UpdatePostDTO dto, Long memberId, Long postId);
    void deletePost(Long postId, Long memberId);
}
