package com.example.teamB.global.upload.handler;

import com.example.teamB.global.upload.exception.UploadErrorCode;
import com.example.teamB.global.upload.exception.UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3UploadHandler implements UploadHandler {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {
        String storeFileName = createStoreFileName(file.getOriginalFilename());

        try {
            uploadFile(file, storeFileName);
        }
        catch (S3Exception | IOException e) {
            throw new UploadException(UploadErrorCode.UPLOAD_FAILED);
        }

        return getFullStoredUrl(storeFileName);
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        if (!ext.equals("jpg") || !ext.equals("png")) {
            throw new UploadException(UploadErrorCode.UPLOAD_FAILED);
        }
        return uuid + "." + ext;
    }

    // 확장자 추출 메서드
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private void uploadFile(MultipartFile file, String storeFileName) throws IOException {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(storeFileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
    }

    private String getFullStoredUrl(String storeFileName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(storeFileName)
                        .build())
                .toString();
    }
}
