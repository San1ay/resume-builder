package com.hisanjay.resumebuilderapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.TemplateResponse;
import com.hisanjay.resumebuilderapi.service.AuthService;
import com.hisanjay.resumebuilderapi.service.TemplateService;
import com.hisanjay.resumebuilderapi.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constants.TEMPLATE_ENDPOINT)
@Slf4j
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication) {

        TemplateResponse templates = templateService.getTemplates(authentication);
        return ResponseEntity.ok(templates);
    }
}
