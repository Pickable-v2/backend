package com.example.eatpick.auth.domain.dto.request;

import com.example.eatpick.auth.domain.type.GenderType;

public record MyPageUpdateRequest(
    String loginId,
    String nickname,
    GenderType gender,
    int age
) {

}