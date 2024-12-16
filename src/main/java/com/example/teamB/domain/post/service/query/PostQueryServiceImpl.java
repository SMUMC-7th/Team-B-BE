package com.example.teamB.domain.post.service.query;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.domain.post.respository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Page<Post> getPostList(Integer page) {
        Page<Post> PostPage = postRepository.findAllById(PageRequest.of(page, 10));
        return PostPage;
    }

}
