package com.marbor.customauthentication.resources.dto;

import lombok.Getter;

@Getter
public class HelloResponse {
    private final String message;

    public HelloResponse(String message) {
        this.message = message;
    }
}
