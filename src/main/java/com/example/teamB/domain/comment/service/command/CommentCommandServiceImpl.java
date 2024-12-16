package com.example.teamB.domain.comment.service.command;

import com.example.teamB.domain.comment.converter.CommentConverter;
import com.example.teamB.domain.comment.dto.CommentCreateRequestDto;
import com.example.teamB.domain.comment.dto.CommentResponseDTO;
import com.example.teamB.domain.comment.dto.CommentUpdateRequestDto;
import com.example.teamB.domain.comment.entity.Comment;
import com.example.teamB.domain.comment.repository.CommentRepository;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.domain.post.respository.PostRepository;
import com.example.teamB.global.apiPayload.code.GeneralErrorCode;
import com.example.teamB.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentConverter commentConverter;

    // 댓글 생성
    public CommentResponseDTO.CommentPreviewDTO createComment(CommentCreateRequestDto request, Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Comment parent = null;
        if (request.getParentId() != 0) {
            parent = commentRepository.findById(request.getParentId()).orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        }

        Comment comment = new Comment(request.getContent(), member, post, parent);
        // 생성 후 저장
        Comment savedComment = commentRepository.save(comment);

        return commentConverter.toCommentPreviewDTO(savedComment, null);
    }

    // 댓글 수정
    public CommentResponseDTO.CommentPreviewDTO updateComment(Long commentId, CommentUpdateRequestDto request, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.FORBIDDEN_403));

        comment.updateContent(request.getContent());

        return commentConverter.toCommentPreviewDTO(comment, null);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.FORBIDDEN_403));

        commentRepository.delete(comment);
    }

}
