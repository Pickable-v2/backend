package com.example.picktable.member.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractUtil {
    private static final String BEARER_TYPE = "Bearer";

    public static String extractToken(String authHeaderValue) {
        if (authHeaderValue.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            return authHeaderValue.substring(BEARER_TYPE.length()).trim();
        }
        return null;
    }
}
