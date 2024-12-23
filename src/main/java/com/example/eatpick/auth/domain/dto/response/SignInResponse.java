package com.example.eatpick.auth.domain.dto.response;

import com.example.eatpick.common.security.domain.dto.JwtToken;

public record SignInResponse(
    String loginId,
    String loginPw,
    String grantType,
    String accessToken,
    String refreshToken
) {

    public static SignInResponse of(String loginId, String loginPw, JwtToken jwtToken) {
        return new SignInResponse(loginId, loginPw, jwtToken.getGrantType(), jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }
}
