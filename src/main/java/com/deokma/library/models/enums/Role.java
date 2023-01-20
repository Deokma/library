package com.deokma.library.models.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Denis Popolamov
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
