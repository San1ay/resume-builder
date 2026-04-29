package com.hisanjay.resumebuilderapi.utils;

public class Constants {
    // AUTH CONTROLLER
    public static final String AUTH_ENDPOINT = "/api/auth";
    public static final String AUTH_LOGIN_ENDPOINT = "/login";
    public static final String AUTH_REGISTER_ENDPOINT = "/register";
    public static final String AUTH_PROFILE_ENDPOINT = "/profile";
    public static final String AUTH_EMAIL_VERIFY_ENDPOINT = "/verify-email";
    public static final String AUTH_EMAIL_VERIFY_RESEND_ENDPOINT = "/resend-verification";
    public static final String AUTH_UPLOAD_ENDPOINT = "/upload";

    // RESUME CONTROLLER
    public static final String RESUME_ENDPOINT = "/api/resume";
    public static final String RESUME_ID_ENDPOINT = "/{id}";
    public static final String RESUME_UPLOAD_IMAGES_ENDPOINT = "/{id}/upload-images";

    private Constants() {
    }
}
