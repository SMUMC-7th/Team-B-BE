package com.example.teamB.domain.ootd.service.command;

import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.ootd.dto.OotdRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface OotdCommandService {
    void createOotd(OotdRequestDTO.CreateOotdDTO dto, MultipartFile image, Member member);
}
