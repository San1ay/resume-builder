package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.RegisterRequest;
import com.hisanjay.resumebuilderapi.service.AuthService;
import com.hisanjay.resumebuilderapi.service.FileUploadService;
import com.hisanjay.resumebuilderapi.service.S3FileUploadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final FileUploadService fileUploadService;
    private final S3FileUploadService s3FileUploadService;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> upload(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "provider", defaultValue = "cloudinary") String provider) throws IOException {

        MultipartFile uploadFile = resolveUploadFile(image, file);

        if (uploadFile == null || uploadFile.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        Map<String, String> response;

        switch (provider.toLowerCase()) {
            case "aws":
            case "s3":
                response = Map.of(
                        "url", s3FileUploadService.upload(uploadFile));
                break;

            case "cloudinary":
            default:
                response = fileUploadService.uploadSingleImage(uploadFile);
                break;
        }

        return ResponseEntity.ok(response);
    }

    private MultipartFile resolveUploadFile(MultipartFile image, MultipartFile file) {
        MultipartFile uploadFile = image != null && !image.isEmpty() ? image : file;

        if (uploadFile == null || uploadFile.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attach a file using multipart/form-data with field name 'image' or 'file'.");
        }

        return uploadFile;
    }

}
