package com.hisanjay.resumebuilderapi.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloundinary.uri}")
    private String cloudinaryUri;

    @Bean
    public Cloudinary cloud() {
        return new Cloudinary(cloudinaryUri);
    }
}