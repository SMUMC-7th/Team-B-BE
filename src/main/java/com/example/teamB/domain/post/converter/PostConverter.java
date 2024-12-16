package com.example.teamB.domain.post.converter;

import com.example.teamB.domain.post.dto.PostResponseDTO;
import com.example.teamB.domain.post.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {
    public static PostResponseDTO.PostPreViewDTO postPreViewDTO(Post post){
        if (post == null || post.getMember() == null) {
            System.out.println("Invalid mission data: " + post);
            return null;
        }
        return PostResponseDTO.PostPreViewDTO.builder()
                .memberName(post.getMember().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public static PostResponseDTO.PostPreViewListDTO postPreViewListDTO(Page<Post> postList){
        List<PostResponseDTO.PostPreViewDTO> postPreViewDTOList = postList.stream()
                .map(PostConverter::postPreViewDTO).collect(Collectors.toList());
        return PostResponseDTO.PostPreViewListDTO.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(postPreViewDTOList.size())
                .postList(postPreViewDTOList)
                .build();

    }

}
