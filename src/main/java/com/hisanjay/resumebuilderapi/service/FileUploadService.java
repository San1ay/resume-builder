package com.hisanjay.resumebuilderapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.model.Resume;
import com.hisanjay.resumebuilderapi.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {
        Map imageUploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "image"));
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }

    public Map<String, String> uploadResumeImages(String id, Authentication authentication, MultipartFile thumbnail,
            MultipartFile profileImage) throws IOException {
        AuthRespone response = authService.getProfile(authentication);
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), id)
                .orElseThrow(() -> new RuntimeException("No resume found"));

        Map<String, String> returnValue = new HashMap<>();
        boolean isUpdated = false;

        if (Objects.nonNull(thumbnail)) {
            Map<String, String> thumbnailResult = uploadSingleImage(thumbnail);
            existingResume.setThumbnailLink(thumbnailResult.get("imageUrl"));
            returnValue.put("thumbnailLink", thumbnailResult.get("imageUrl"));
            isUpdated = true;
        }

        if (Objects.nonNull(profileImage)) {
            Map<String, String> profileResult = uploadSingleImage(profileImage);
            existingResume.getProfileInfo().setProfilePreviewUrl(profileResult.get("imageUrl"));
            returnValue.put("profilePreviewUrl", profileResult.get("imageUrl"));
            isUpdated = true;

        }
        if (isUpdated) {
            resumeRepository.save(existingResume);
            returnValue.put("message", "Image Uploaded Sucessfully.");
        }

        return returnValue;
    }

}
