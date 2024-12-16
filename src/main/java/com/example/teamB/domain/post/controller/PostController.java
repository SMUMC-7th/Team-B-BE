package com.example.teamB.domain.post.controller;


import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.post.converter.PostConverter;
import com.example.teamB.domain.post.dto.PostRequestDTO;
import com.example.teamB.domain.post.entity.Post;
import com.example.teamB.domain.post.service.command.PostCommandService;
import com.example.teamB.domain.post.service.query.PostQueryService;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "POST API")
public class PostController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    //게시물 등록
    @PostMapping()
    @Operation(summary = "게시물 등록", description = "사용자가 오늘의 게시물 기록 등록")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER404", description = "조회되는 멤버가 없습니다. 토큰을 확인해주세요",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<Long> createPost(
            @CurrentMember Member member,
            @RequestBody PostRequestDTO.CreatePostDTO request) {
        Long postId=postCommandService.createPost(request, member.getId());
        return CustomResponse.onSuccess(postId);
    }

    //게시물 전체 조회
    @GetMapping()
    @Operation(summary = "게시물 전체 조회", description = "게시물 전체 조회하는 api")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER404", description = "조회되는 멤버가 없습니다. 토큰을 확인해주세요",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<?> getPostList(
            @RequestParam(name = "page") Integer page) {
        page-=1;
        Page<Post> postList=postQueryService.getPostList(page);
        return CustomResponse.onSuccess(PostConverter.postPreViewListDTO(postList));
    }

    //게시물 단일 조회
    @GetMapping("/{postId}")
    @Operation(summary = "게시물 단일 조회", description = "게시물 단일 조회하는 api")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST002", description = "해당 게시물이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 아이디, path variable 입니다")
    })
    public CustomResponse<?> getPost(@PathVariable Long postId) {
        Post post = postQueryService.getPost(postId);
        return CustomResponse.onSuccess(post);
    }

    //게시물 수정
    @PatchMapping("/{postId}")
    @Operation(summary = "게시물 수정", description = "게시물 수정하는 api")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST002", description = "해당 게시물이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 아이디, path variable 입니다")
    })
    public CustomResponse<?> patchPost(
            @CurrentMember Member member,
            @PathVariable Long postId,
            @RequestPart PostRequestDTO.CreatePostDTO dto) {
        postCommandService.updatePost(dto, member.getId(),postId);
        return CustomResponse.onSuccess(null);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시물 삭제", description = "게시물 삭제하는 api")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST002", description = "해당 게시물이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 아이디, path variable 입니다")
    })
    public CustomResponse<?> deletePost(
            @CurrentMember Member member,
            @PathVariable Long postId) {
        postCommandService.deletePost(postId, member.getId());
        return CustomResponse.onSuccess(null);
    }
}
