package com.hisanjay.resumebuilderapi.enums;

import java.util.Arrays;

public enum UploadType {

    S3("s3", "aws"),
    CLOUDINARY("cloudinary");

    private final String[] aliases;

    UploadType(String... aliases) {
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static UploadType from(String provider) {
        if (provider == null || provider.isBlank()) {
            return S3;
        }

        return Arrays.stream(values())
                .filter(type -> Arrays.stream(type.aliases)
                        .anyMatch(alias -> alias.equalsIgnoreCase(provider)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid upload provider: " + provider));
    }
}