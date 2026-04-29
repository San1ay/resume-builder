package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hisanjay.resumebuilderapi.dto.CreateResumeRequest;
import com.hisanjay.resumebuilderapi.model.Resume;
import com.hisanjay.resumebuilderapi.service.FileUploaderService;
import com.hisanjay.resumebuilderapi.service.ResumeService;
import com.hisanjay.resumebuilderapi.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.RESUME_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;
    private final FileUploaderService fileUploaderService;

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest createResumeRequest,
            Authentication authentication) {
        Resume newResume = resumeService.createResume(createResumeRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @GetMapping
    public ResponseEntity<?> getUserResumes(Authentication authentication) {
        List<Resume> resumes = resumeService.getUserResumes(authentication);
        return ResponseEntity.ok(resumes);

    }

    @GetMapping(Constants.RESUME_ID_ENDPOINT)
    public ResponseEntity<?> getResumeById(@PathVariable String id, Authentication authentication) {
        Resume existingResume = resumeService.getResumeById(id, authentication);
        return ResponseEntity.ok(existingResume);

    }

    @PatchMapping(Constants.RESUME_ID_ENDPOINT)
    public ResponseEntity<?> updateResume(@PathVariable String id, @RequestBody Resume updatedData,
            Authentication authentication) {
        Resume udpatedResume = resumeService.updateResume(id, updatedData, authentication);
        return ResponseEntity.ok(udpatedResume);

    }

    @PutMapping(Constants.RESUME_UPLOAD_IMAGES_ENDPOINT)
    public ResponseEntity<?> uploadResumeImage(@PathVariable String id,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request, Authentication authentication) throws IOException {
        Map<String, String> response = fileUploaderService.uploadResumeImages(id, authentication, thumbnail,
                profileImage);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping(Constants.RESUME_ID_ENDPOINT)
    public ResponseEntity<?> deleteResume(@PathVariable String id, Authentication authentication) {
        resumeService.deleteResume(id, authentication);
        return ResponseEntity.ok(Map.of("message", "Resume Deleted Successfully"));

    }
}
