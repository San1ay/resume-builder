package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hisanjay.resumebuilderapi.dto.LoginRequest;
import com.hisanjay.resumebuilderapi.dto.RegisterRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Authentication API", description = "Endpoints for authentication, profile management, email verification, and file uploads")
public interface AuthControllerSpec {

        @Operation(summary = "Register User", description = "Registers a new user account")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid request")
        })
        ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request);

        @Operation(summary = "Login User", description = "Authenticate user and return JWT token")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Login successful"),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials")
        })
        ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest);

        @Operation(summary = "Get Profile", description = "Fetch authenticated user profile")
        @SecurityRequirement(name = "bearerAuth")
        @ApiResponse(responseCode = "200", description = "Profile fetched successfully")
        ResponseEntity<?> getProfile(Authentication authentication);

        @Operation(summary = "Resend Verification Email", description = "Resend email verification link")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Verification email sent"),
                        @ApiResponse(responseCode = "400", description = "Email is required")
        })
        ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body);

        @Operation(summary = "Verify Email", description = "Verify user email using token")
        @ApiResponse(responseCode = "200", description = "Email verified successfully")
        ResponseEntity<?> verifyEmail(
                        @Parameter(description = "Verification token") @RequestParam String token);

        @Operation(summary = "Upload File", description = "Upload image/file to configured provider")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                        @ApiResponse(responseCode = "400", description = "File missing")
        })
        ResponseEntity<Map<String, String>> upload(
                        @Parameter(description = "Image file") MultipartFile image,

                        @Parameter(description = "Generic file") MultipartFile file,

                        @Parameter(description = "Upload provider") String provider) throws IOException;
}