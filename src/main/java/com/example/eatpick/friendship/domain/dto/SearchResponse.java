package com.example.eatpick.friendship.domain.dto;

import com.example.eatpick.auth.domain.entity.Member;

public record SearchResponse(
    String loginId,
    String nickname
) {

    public static SearchResponse from(Member member) {
        return new SearchResponse(member.getLoginId(), member.getNickname());
    }
}
