package com.example.eatpick.auth.domain.dto.request;

import java.util.List;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;
import com.example.eatpick.memberPreferencesTaste.domain.entity.MemberPreferencesTaste;
import com.example.eatpick.preferencesTaste.domain.entity.PreferencesTaste;
import com.example.eatpick.preferencesTaste.domain.type.PreferencesType;

public record MemberRequest(
    String loginId,
    String loginPw,
    String verifiedLoginPw,
    String nickname,
    GenderType gender,
    int age,
    List<PreferencesType> preferences
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
