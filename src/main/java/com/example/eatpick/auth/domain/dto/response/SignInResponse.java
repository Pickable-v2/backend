package com.example.eatpick.auth.domain.dto.response;

import com.example.eatpick.common.security.domain.dto.JwtToken;

import lombok.Getter;

@Getter
public class SignInResponse {
    private String loginId;
    private String loginPw;
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public SignInResponse(String loginId, String loginPw, JwtToken jwtToken) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.grantType = jwtToken.getGrantType();
        this.accessToken = jwtToken.getAccessToken();
        this.refreshToken = jwtToken.getRefreshToken();
    }
}
