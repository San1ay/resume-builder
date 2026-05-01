package com.hisanjay.resumebuilderapi.enums;

public enum SubType {

    BASIC("Basic"),
    PREMIUM("Premium");

    private final String value;

    SubType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}