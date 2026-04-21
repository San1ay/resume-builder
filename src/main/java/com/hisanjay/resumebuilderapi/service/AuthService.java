package com.hisanjay.resumebuilderapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hisanjay.resumebuilderapi.document.User;
import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.LoginRequest;
import com.hisanjay.resumebuilderapi.dto.RegisterRequest;
import com.hisanjay.resumebuilderapi.exception.ResourceExistsException;
import com.hisanjay.resumebuilderapi.repository.UserRepository;
import com.hisanjay.resumebuilderapi.utils.Jwtutil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutil jwtutil;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public AuthRespone register(RegisterRequest request) {
        log.info("inside AuthSerive: register() {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("User already Exists with email");
        }

        User newUser = toDocument(request);
        userRepository.save(newUser);

        // sendVerificationEmail(newUser);

        return toResponse(newUser);

    }

    public AuthRespone login(LoginRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Invalid Email or Password"));

        // Fix: matches(raw, encoded)
        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid Email or Password");
        }

        if (!existingUser.getEmailVerified()) {
            throw new RuntimeException("Please Verify Email before logging in.");
        }

        String token = jwtutil.generateToken(existingUser.getEmail());
        AuthRespone response = toResponse(existingUser);
        response.setToken(token);
        return response;
    }

    public void verifyEmail(String verificationToken) {
        User user = userRepository.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new RuntimeException("Verificaton Token Invalid or expired"));

        if (user != null && user.getVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verifcation Token is expired. Please request new token");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);

    }

    private void sendVerificationEmail(User newUser) {
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + newUser.getVerificationToken();
            String html = """
                    <table width="100%" cellpadding="0" cellspacing="0" style="font-family:Arial,sans-serif;">
                      <tr>
                        <td>
                          <p>Hi <strong>{{USER_NAME}}</strong>,</p>
                          <p>Please verify your email by clicking below:</p>

                          <a href="{{VERIFY_LINK}}"
                             style="display:inline-block;padding:10px 16px;background:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;">
                             Verify Email
                          </a>

                          <p style="font-size:12px;margin-top:15px;">
                            Or use this link:<br/>
                            <a href="{{VERIFY_LINK}}">{{VERIFY_LINK}}</a>
                          </p>
                        </td>
                      </tr>
                    </table>

                                        """
                    .replace("{{USER_NAME}}", newUser.getName())
                    .replace("{{VERIFY_LINK}}", link);
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify Your Email", html);

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Failed to send Verification Email: " + e.getMessage());
        }
    }

    private User toDocument(RegisterRequest request) {
        return User.builder()
                .name(request.getName()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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
