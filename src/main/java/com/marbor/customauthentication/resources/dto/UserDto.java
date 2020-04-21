package com.marbor.customauthentication.resources.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDto {
    private final String name;
    private final String department;
}
