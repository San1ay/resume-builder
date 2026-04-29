package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.hisanjay.resumebuilderapi.dto.CreateResumeRequest;
import com.hisanjay.resumebuilderapi.model.Resume;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Resume API", description = "Endpoints for managing resumes, uploading images, and deleting resumes")
@SecurityRequirement(name = "bearerAuth")
public interface ResumeControllerSpec {

    @Operation(summary = "Create Resume", description = "Creates a new resume for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resume created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    ResponseEntity<?> createResume(
            @Valid @RequestBody CreateResumeRequest createResumeRequest,
            Authentication authentication);

    @Operation(summary = "Get User Resumes", description = "Fetch all resumes belonging to authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumes fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> getUserResumes(Authentication authentication);

    @Operation(summary = "Get Resume By ID", description = "Fetch a specific resume by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Resume not found")
    })
    ResponseEntity<?> getResumeById(
            @Parameter(description = "Resume ID") @PathVariable String id,
            Authentication authentication);

    @Operation(summary = "Update Resume", description = "Update an existing resume")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume updated successfully"),
            @ApiResponse(responseCode = "404", description = "Resume not found")
    })
    ResponseEntity<?> updateResume(
            @Parameter(description = "Resume ID") @PathVariable String id,
            @RequestBody Resume updatedData,
            Authentication authentication);

    @Operation(summary = "Upload Resume Images", description = "Upload thumbnail and profile image for resume")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file upload request")
    })
    ResponseEntity<?> uploadResumeImage(
            @Parameter(description = "Resume ID") @PathVariable String id,

            @Parameter(description = "Resume thumbnail image") @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,

            @Parameter(description = "Profile image") @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,

            Authentication authentication) throws IOException;

    @Operation(summary = "Delete Resume", description = "Delete a resume by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Resume not found")
    })
    ResponseEntity<?> deleteResume(
            @Parameter(description = "Resume ID") @PathVariable String id,
            Authentication authentication);
}