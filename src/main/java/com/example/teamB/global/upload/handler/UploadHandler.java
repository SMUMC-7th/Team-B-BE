package com.example.teamB.global.upload.handler;

import org.springframework.web.multipart.MultipartFile;

/**
 * 저장 방식 변경을 대비한 인터페이스
 */
public interface UploadHandler {
    void upload(MultipartFile file, String storeImageName);
    String getUrl(String fileName);
}
