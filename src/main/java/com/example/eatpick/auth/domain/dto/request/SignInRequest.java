package com.example.eatpick.auth.domain.dto.request;

public record SignInRequest(
    String loginId,
    String loginPw
) {

}
