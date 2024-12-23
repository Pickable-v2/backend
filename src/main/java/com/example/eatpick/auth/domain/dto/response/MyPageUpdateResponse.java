package com.example.eatpick.auth.domain.dto.response;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;

public record MyPageUpdateResponse(
    String nickname,
    GenderType gender,
    int age
) {

    public static MyPageUpdateResponse from(Member member) {
        return new MyPageUpdateResponse(member.getNickname(), member.getGender(), member.getAge());
    }
}
