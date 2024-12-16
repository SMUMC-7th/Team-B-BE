package com.example.teamB.domain.post.service.query;

import com.example.teamB.domain.post.entity.Post;
import org.springframework.data.domain.Page;

public interface PostQueryService {
    Page<Post> getPostList(Integer page);
    Post getPost(Long postId);
}
