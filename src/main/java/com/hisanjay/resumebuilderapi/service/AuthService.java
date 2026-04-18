package com.hisanjay.resumebuilderapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hisanjay.resumebuilderapi.document.User;
import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.RegisterRequest;
import com.hisanjay.resumebuilderapi.exception.ResourceExistsException;
import com.hisanjay.resumebuilderapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    public AuthRespone register(RegisterRequest request) {
        log.info("inside AuthSerive: register() {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("User already Exists with email");
        }

        User newUser = toDocument(request);
        userRepository.save(newUser);

        // TODO; Send email

        return toResponse(newUser);

    }

    private User toDocument(RegisterRequest request) {
        return User.builder()
                .name(request.getName()).email(request.getEmail()).password(request.getPassword())
                .profileImageUrl(request.getProfileImageUrl()).emailVerified(false).subscriptionPlan("Basic")
                .verificationToken(UUID.randomUUID().toString()).verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    private AuthRespone toResponse(User newUser) {
        return AuthRespone.builder().id(newUser.getId()).name(newUser.getName()).email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl()).emailVerified(newUser.getEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan()).createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt()).build();
    }
}
