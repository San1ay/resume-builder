package com.hisanjay.resumebuilderapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileUploadService {

    private final S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucket;

    public String upload(MultipartFile file) throws IOException {

        String key = "resume-builder/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return getFileUrl(key);
    }

    private String getFileUrl(String key) {
        return "https://" + bucket + ".s3.ap-south-1.amazonaws.com/" + key;
    }
}