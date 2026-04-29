package com.hisanjay.resumebuilderapi.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.hisanjay.resumebuilderapi.enums.UploadType;
import com.hisanjay.resumebuilderapi.model.Resume;
import com.hisanjay.resumebuilderapi.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploaderService {
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    // @Qualifier("s3Uploader") default
    private final FileUploadService s3Uploader;

    // @Autowired - Qualifier resolved by lombok.config instead
    @Qualifier("cloudinaryUploader")
    private final FileUploadService cloudinaryUploader;

    public String uploadToS3(MultipartFile file) throws IOException {
        return s3Uploader.upload(file);
    }

    public String uploadToCloudinary(MultipartFile file) throws IOException {
        return cloudinaryUploader.upload(file);
    }

    public String upload(MultipartFile file, UploadType uploadType) throws IOException {

        UploadType finalType = Optional.ofNullable(uploadType)
                .orElse(UploadType.S3);

        return switch (finalType) {
            case S3 -> s3Uploader.upload(file);
            case CLOUDINARY -> cloudinaryUploader.upload(file);
        };
    }

    public Map<String, String> uploadResumeImages(String id, Authentication authentication, MultipartFile thumbnail,
            MultipartFile profileImage) throws IOException {
        String userId = authService.getUserId(authentication);
        Resume existingResume = resumeRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("No resume found"));

        Map<String, String> returnValue = new HashMap<>();
        boolean isUpdated = false;

        if (Objects.isNull(thumbnail) && Objects.isNull(profileImage)) {
            throw new RuntimeException("Provide thumbnail or profileImage");
        }

        if (Objects.nonNull(thumbnail)) {
            String thumbnailUrl = upload(thumbnail, null);
            existingResume.setThumbnailLink(thumbnailUrl);
            returnValue.put("thumbnailLink", thumbnailUrl);
            isUpdated = true;
        }

        if (Objects.nonNull(profileImage)) {
            String profileUrl = upload(profileImage, null);
            existingResume.getProfileInfo().setProfilePreviewUrl(profileUrl);
            returnValue.put("profilePreviewUrl", profileUrl);
            isUpdated = true;

        }
        if (isUpdated) {
            resumeRepository.save(existingResume);
            returnValue.put("message", "Image Uploaded Sucessfully.");
        }

        return returnValue;
    }
}
