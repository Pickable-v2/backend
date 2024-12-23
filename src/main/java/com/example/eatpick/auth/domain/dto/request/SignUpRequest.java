package com.example.eatpick.auth.domain.dto.request;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    private String loginId;
    private String loginPw;
    private String verifiedLoginPw;
    private String nickname;
    private GenderType gender;
    private int age;

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
