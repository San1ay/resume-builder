package com.hisanjay.resumebuilderapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRespone {
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    private Boolean emailVerified;
    private String subscriptionPlan;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
