package com.example.teamB.domain.post.service.command;

import com.example.teamB.domain.post.dto.PostRequestDTO;

public interface PostCommandService {
    void createPost(PostRequestDTO.CreatePostDTO dto, Long memberId);
}
