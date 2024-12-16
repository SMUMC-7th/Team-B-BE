package com.example.teamB.domain.post.service.command;


import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.post.dto.PostRequestDTO;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.domain.post.exception.PostErrorCode;
import com.example.teamB.domain.post.exception.PostException;
import com.example.teamB.domain.post.respository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Long createPost(PostRequestDTO.CreatePostDTO dto, Long memberId) {

        // 멤버 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 게시물 저장
        Post post= Post.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        postRepository.save(post);

        return post.getId();
    }

    @Override
    public void updatePost(PostRequestDTO.CreatePostDTO dto, Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_REGISTRATION_FAILED));
        if (!post.getMember().getId().equals(memberId)) {
           throw new PostException(PostErrorCode.UNAUTH_FAILED);
        }

        post.update(dto.getTitle(), dto.getContent());
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_REGISTRATION_FAILED));
        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.UNAUTH_FAILED);
        }

        postRepository.delete(post);
    }



}
