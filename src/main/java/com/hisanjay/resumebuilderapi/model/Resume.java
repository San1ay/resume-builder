package com.hisanjay.resumebuilderapi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "resumes")
public class Resume {

    @Id
    @JsonProperty("_id")
    private String id;

    private String userId;
    private String title;
    private String thumbnailLink;
    @Builder.Default
    private Template template = new Template();

    @Builder.Default
    private ProfileInfo profileInfo = new ProfileInfo();

    @Builder.Default
    private ContactInfo contactInfo = new ContactInfo();

    @Builder.Default
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @Builder.Default
    private List<Education> educations = new ArrayList<>();

    @Builder.Default
    private List<Skill> skills = new ArrayList<>();

    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    @Builder.Default
    private List<Certification> certifications = new ArrayList<>();

    @Builder.Default
    private List<Language> languages = new ArrayList<>();

    @Builder.Default
    private List<String> interests = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Template {
        private String theme;

        @Builder.Default
        private List<String> colorPalette = new ArrayList<>();

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileInfo {
        private String profilePreviewUrl;
        private String fullName;
        private String designation;
        private String summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactInfo {
        private String email;
        private String phone;
        private String linkedIn;
        private String github;
        private String website;
        private String location;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkExperience {
        private String company;
        private String role;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Education {
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Skill {
        private String name;
        private Integer progress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Project {
        private String title;
        private String github;
        private String description;
        private String liveDemo;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Certification {
        private String title;
        private String issuer;
        private String year;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Language {
        private String name;
        private Integer progress;

    }

}
