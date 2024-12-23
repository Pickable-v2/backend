package com.example.eatpick.auth.domain.dto.response;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;

public record MemberResponse(
    Long id,
    String loginId,
    String loginPw,
    String verifiedLoginPw,
    String nickname,
    GenderType gender,
    RoleType role,
    int age
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getLoginId(), member.getLoginPw(), member.getVerifiedLoginPw(),
            member.getNickname(), member.getGender(), member.getRole(), member.getAge());
    }
}
