package com.marbor.customauthentication.security;

import org.springframework.security.core.AuthenticationException;

class NotAllowedDepartmentException extends AuthenticationException {

    public NotAllowedDepartmentException() {
        super("Not allowed department");
    }
}
