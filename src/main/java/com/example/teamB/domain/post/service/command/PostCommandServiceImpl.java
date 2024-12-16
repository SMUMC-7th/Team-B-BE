package com.example.teamB.domain.post.service.command;


import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.post.dto.PostRequestDTO;
import com.example.teamB.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final MemberRepository memberRepository;

    @Override
    public void createPost(PostRequestDTO.CreatePostDTO dto, Long memberId) {

        // 멤버 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 게시물 저장
        Post post= Post.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}
