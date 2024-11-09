package com.example.teamB.global.upload.handler;

import com.example.teamB.global.upload.error.UploadErrorCode;
import com.example.teamB.global.upload.error.UploadException;
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

@Service
@RequiredArgsConstructor
@Transactional
public class S3UploadHandler implements UploadHandler {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public void upload(MultipartFile file, String storeImageName) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(storeImageName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        }
        catch (S3Exception | IOException e) {
            throw new UploadException(UploadErrorCode.UPLOAD_FAILED);
        }
    }

    @Override
    public String getUrl(String fileName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build())
                .toString();
    }
}
