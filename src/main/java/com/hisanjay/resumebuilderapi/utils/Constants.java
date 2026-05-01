package com.hisanjay.resumebuilderapi.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    // AUTH CONTROLLER
    public final String AUTH_ENDPOINT = "/api/auth";
    public final String AUTH_LOGIN_ENDPOINT = "/login";
    public final String AUTH_REGISTER_ENDPOINT = "/register";
    public final String AUTH_PROFILE_ENDPOINT = "/profile";
    public final String AUTH_EMAIL_VERIFY_ENDPOINT = "/verify-email";
    public final String AUTH_EMAIL_VERIFY_RESEND_ENDPOINT = "/resend-verification";
    public final String AUTH_UPLOAD_ENDPOINT = "/upload";

    // RESUME CONTROLLER
    public final String RESUME_ENDPOINT = "/api/resume";
    public final String RESUME_ID_ENDPOINT = "/{id}";
    public final String RESUME_UPLOAD_IMAGES_ENDPOINT = "/{id}/upload-images";

    // TEMPLATE CONTROLLER
    public final String TEMPLATE_ENDPOINT = "/api/templates";

}
