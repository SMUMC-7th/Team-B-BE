package com.example.teamB.domain.comment.service.query;

import com.example.teamB.domain.comment.converter.CommentConverter;
import com.example.teamB.domain.comment.dto.CommentResponseDTO;
import com.example.teamB.domain.comment.entity.Comment;
import com.example.teamB.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    // 커서 기반 댓글 조회
    public CommentResponseDTO.CommentPreviewListDTO getComments(Long postId, Long cursorId, int size) {

        // 최상위 댓글 조회 (커서 기반)
        Pageable pageable = PageRequest.of(0, size); // Fetch 10 results
        List<Comment> topLevelComments = commentRepository.findTopLevelCommentsByCursor(postId, cursorId, pageable);

        // 댓글 트리 구조로 변환
        List<CommentResponseDTO.CommentPreviewDTO> comments = topLevelComments.stream()
                .map(this::buildCommentTree)
                .collect(Collectors.toList());

        return commentConverter.toCommentPreviewListDTO(comments);
    }

    // 재귀적으로 대댓글 트리 구조 생성, 부모 댓글의 대댓글을 한번에 조회
    private CommentResponseDTO.CommentPreviewDTO buildCommentTree(Comment comment) {
        // 부모 댓글들의 대댓글을 한 번에 조회
        List<Comment> allChildren = commentRepository.findAllChildrenByParentComments(List.of(comment));

        // 자식 댓글을 필터링하여 트리 구조로 재귀적으로 처리
        Map<Long, List<Comment>> childrenMap = allChildren.stream()
                .collect(Collectors.groupingBy(c -> c.getParent().getId()));

        List<CommentResponseDTO.CommentPreviewDTO> childResponses = childrenMap.getOrDefault(comment.getId(), List.of())
                .stream()
                .map(this::buildCommentTree)
                .collect(Collectors.toList());

        return commentConverter.toCommentPreviewDTO(comment, childResponses);
    }
}
