package com.hisanjay.resumebuilderapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hisanjay.resumebuilderapi.service.EmailService;
import com.hisanjay.resumebuilderapi.utils.Constants;
import com.mongodb.lang.NonNull;

import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(Constants.EMAIL_ENDPOINT)
public class EmailController {

    private final EmailService emailService;

    @PostMapping(value = "/send-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Map<String, Object>> sendResumeByEmail(
            @NonNull @RequestPart("recipientEmail") String recipientEmail,
            @RequestPart("subject") String subject, @RequestPart("message") String message,
            @RequestPart("pdfFile") MultipartFile pdfFile, Authentication authentication)
            throws IOException, MessagingException {

        Map<String, Object> response = new HashMap<>();
        if (Objects.isNull(recipientEmail) || Objects.isNull(pdfFile)) {
            response.put("success", false);
            response.put("message", "Missing required Fields.");

            return ResponseEntity.badRequest().body(response);
        }

        byte[] pdfBytes = pdfFile.getBytes();
        String originalFilename = pdfFile.getOriginalFilename();
        String fileName = Objects.nonNull(originalFilename) ? originalFilename : "resume.pdf";

        String emailSubject = Objects.nonNull(subject) ? subject : "Resume Application";
        String emailBody = Objects.nonNull(message) ? message : "Please Find attached Resume";

        emailService.sendEmailWithAttachment(recipientEmail, emailSubject, emailBody, pdfBytes, fileName);

        response.put("success", true);
        response.put("message", "Resume Sent Successfully to " + recipientEmail);

        return ResponseEntity.ok(response);

    }

}
