package com.example.eatpick.auth.domain.dto.response;

import java.util.List;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;
import com.example.eatpick.preferencesTaste.domain.type.PreferencesType;

public record MemberResponse(
    Long id,
    String loginId,
    String loginPw,
    String verifiedLoginPw,
    String nickname,
    GenderType gender,
    RoleType role,
    int age,
    List<PreferencesType> preferences
) {

    public static MemberResponse from(Member member) {
        List<PreferencesType> preferences = member.getPreferences().stream()
            .map(preference -> preference.getPreferencesTaste().getPreferencesType())
            .toList();

        return new MemberResponse(member.getId(), member.getLoginId(), member.getLoginPw(), member.getVerifiedLoginPw(),
            member.getNickname(), member.getGender(), member.getRole(), member.getAge(), preferences);
    }
}
