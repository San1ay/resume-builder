package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.RegisterRequest;
import com.hisanjay.resumebuilderapi.service.AuthService;
import com.hisanjay.resumebuilderapi.service.FileUploadService;

import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FileUploadService fileUploadService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        AuthRespone response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Email verified Sucessfully"));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file) throws IOException {
        Map<String, String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.ok(response);
    }

}
