package com.example.teamB.domain.ootd.service.command;

import com.example.teamB.domain.hashtag.entity.Hashtag;
import com.example.teamB.domain.hashtag.enums.HashtagCategory;
import com.example.teamB.domain.hashtag.exception.HashtagErrorCode;
import com.example.teamB.domain.hashtag.exception.HashtagException;
import com.example.teamB.domain.hashtag.repository.HashtagRepository;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.ootd.dto.OotdRequestDTO;
import com.example.teamB.domain.ootd.entity.Ootd;
import com.example.teamB.domain.ootd.exception.OotdErrorCode;
import com.example.teamB.domain.ootd.exception.OotdException;
import com.example.teamB.domain.ootd.provider.WeatherClassificationProvider;
import com.example.teamB.domain.ootd.repository.OotdRepository;
import com.example.teamB.domain.ootdHashtag.entity.OotdHashtag;
import com.example.teamB.global.upload.handler.UploadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class OotdCommandServiceImpl implements OotdCommandService {

    private final UploadHandler uploadHandler;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;
    private final OotdRepository ootdRepository;

    @Override
    public void createOotd(OotdRequestDTO.CreateOotdDTO dto, MultipartFile image, Long memberId) {
        // 멤버 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 이미지 업로드
        String storedImageUrl = uploadHandler.upload(image);

        // 날씨 분류 확인
        WeatherClassificationProvider weatherClassification
                = WeatherClassificationProvider.getWeatherClassification(dto.getMinTemperature(), dto.getMaxTemperature());

        // OOTD 저장
        Ootd ootd = Ootd.builder()
                .member(member)
                .image(storedImageUrl)
                .minTemperature(dto.getMinTemperature())
                .maxTemperature(dto.getMaxTemperature())
                .weatherDescription(weatherClassification.getWeatherDescription())
                .ootdHashtags(new ArrayList<>())
                .build();

        // 해시태그 연결
        for (String hashtag : dto.getHashtags()) {
            OotdHashtag ootdHashtag = OotdHashtag.builder()
                    .ootd(ootd)
                    .hashtag(convertHashtag(hashtag))
                    .build();
            ootd.getOotdHashtags().add(ootdHashtag);
        }
        
        ootdRepository.save(ootd);
    }

    private Hashtag convertHashtag(String hashtagName) {
        HashtagCategory category = HashtagCategory.valueOf(hashtagName.toUpperCase());
        return hashtagRepository.findByCategory(category)
                .orElseThrow(() -> new HashtagException(HashtagErrorCode.HASHTAG_NOT_FOUND));
    }
}
