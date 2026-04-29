package com.hisanjay.resumebuilderapi.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.CreateResumeRequest;
import com.hisanjay.resumebuilderapi.model.Resume;
import com.hisanjay.resumebuilderapi.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    public Resume createResume(CreateResumeRequest createResumeRequest, Authentication authentication) {
        AuthRespone authResponse = authService.getProfile(authentication);
        Resume newResume = Resume.builder()
                .userId(authResponse.getId())
                .title(createResumeRequest.getTitle())
                .build();

        return resumeRepository.save(newResume);
    }

    public List<Resume> getUserResumes(Authentication authentication) {
        AuthRespone authRespone = authService.getProfile(authentication);
        return resumeRepository.findByUserId(authRespone.getId());
    }

    public Resume getResumeById(String resumeId, Authentication authentication) {
        AuthRespone authRespone = authService.getProfile(authentication);
        Resume resume = resumeRepository.findByUserIdAndId(authRespone.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return resume;
    }

    public Resume updateResume(String resumeId, Resume updatedData, Authentication authentication) {
        AuthRespone authRespone = authService.getProfile(authentication);
        Resume exsitingResume = resumeRepository.findByUserIdAndId(authRespone.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        updateResumeFields(exsitingResume, updatedData);
        resumeRepository.save(exsitingResume);
        return exsitingResume;
    }

    private void updateResumeFields(Resume existing, Resume updated) {
        if (updated.getTitle() != null)
            existing.setTitle(updated.getTitle());

        if (updated.getThumbnailLink() != null)
            existing.setThumbnailLink(updated.getThumbnailLink());

        if (updated.getTemplate() != null)
            existing.setTemplate(updated.getTemplate());

        if (updated.getProfileInfo() != null)
            existing.setProfileInfo(updated.getProfileInfo());

        if (updated.getContactInfo() != null)
            existing.setContactInfo(updated.getContactInfo());

        if (updated.getProjects() != null)
            existing.setProjects(updated.getProjects());

        if (updated.getWorkExperiences() != null)
            existing.setWorkExperiences(updated.getWorkExperiences());

        if (updated.getEducations() != null)
            existing.setEducations(updated.getEducations());

        if (updated.getSkills() != null)
            existing.setSkills(updated.getSkills());

        if (updated.getLanguages() != null)
            existing.setLanguages(updated.getLanguages());

        if (updated.getInterests() != null)
            existing.setInterests(updated.getInterests());

        if (updated.getCertifications() != null)
            existing.setCertifications(updated.getCertifications());
    }

    public void deleteResume(String resumeId, Authentication authentication) {
        AuthRespone authRespone = authService.getProfile(authentication);
        Resume exsitingResume = resumeRepository.findByUserIdAndId(authRespone.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        resumeRepository.delete(exsitingResume);
    }

}
