package com.hisanjay.resumebuilderapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResponse {
    private List<String> allTemplates;
    private List<String> availableTemplates;
    private String subscriptionPlan;
    private boolean isPremium;
}