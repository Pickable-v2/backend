package com.example.eatpick.auth.domain.dto.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String loginId;
    private String loginPw;
}
