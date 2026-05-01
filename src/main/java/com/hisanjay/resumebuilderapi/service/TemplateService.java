package com.hisanjay.resumebuilderapi.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hisanjay.resumebuilderapi.dto.AuthRespone;
import com.hisanjay.resumebuilderapi.dto.TemplateResponse;
import com.hisanjay.resumebuilderapi.enums.SubType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {
    private final AuthService authService;

    public TemplateResponse getTemplates(Authentication authentication) {
        AuthRespone authRespone = authService.getProfile(authentication);

        boolean isPremium = SubType.PREMIUM.getValue()
                .equalsIgnoreCase(authRespone.getSubscriptionPlan());

        List<String> allTemplates = List.of("01", "02");
        List<String> freeTemplates = List.of("01");

        List<String> availableTemplates = isPremium ? allTemplates : freeTemplates;

        return TemplateResponse.builder()
                .allTemplates(allTemplates)
                .availableTemplates(availableTemplates)
                .subscriptionPlan(authRespone.getSubscriptionPlan())
                .isPremium(isPremium)
                .build();
    }
}
