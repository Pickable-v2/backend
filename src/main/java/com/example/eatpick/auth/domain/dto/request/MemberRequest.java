package com.example.eatpick.auth.domain.dto.request;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;

public record MemberRequest(
    String loginId,
    String loginPw,
    String verifiedLoginPw,
    String nickname,
    GenderType gender,
    int age
) {

    public Member toEntity() {
        return Member.builder()
            .loginId(loginId)
            .loginPw(loginPw)
            .verifiedLoginPw(verifiedLoginPw)
            .nickname(nickname)
            .gender(gender)
            .role(RoleType.USER)
            .age(age)
            .build();
    }
}
