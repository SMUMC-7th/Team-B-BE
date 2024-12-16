package com.example.teamB.domain.ootd.dto;

import com.example.teamB.domain.hashtag.enums.HashtagCategory;
import com.example.teamB.domain.hashtag.exception.HashtagErrorCode;
import com.example.teamB.domain.hashtag.exception.HashtagException;
import com.example.teamB.domain.ootd.exception.OotdErrorCode;
import com.example.teamB.domain.ootd.exception.OotdException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class OotdRequestDTO {

    @Getter
    public static class CreateOotdDTO {
        @NotNull
        private Integer maxTemperature;

        @NotNull
        private Integer minTemperature;

        @NotNull
        private List<String> hashtags;

        public void validateHashtags() {
            // 3개 초과 시 오류 발생
            if (hashtags.size() > 3) {
                throw new OotdException(OotdErrorCode.HASHTAG_LIMIT_EXCEEDED);
            }

            // 모든 해시태그가 HashtagCategory에 포함되는지 검증
            for (String hashtag : hashtags) {
                boolean isValid = false;
                for (HashtagCategory category : HashtagCategory.values()) {
                    if (category.name().equalsIgnoreCase(hashtag)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    throw new HashtagException(HashtagErrorCode.HASHTAG_NOT_FOUND);
                }
            }
        }
    }


}
