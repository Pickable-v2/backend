package com.example.picktable.friendship.domain.dto;

import com.example.picktable.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendListResponseDTO {
    private String loginId;
    private String nickname;

    @Builder
    public FriendListResponseDTO(String loginId, String nickname) {
        this.loginId = loginId;
        this.nickname = nickname;
    }

    /* Entity -> DTO */
    public FriendListResponseDTO(Member member) {
        this.loginId = member.getLoginId();
        this.nickname = member.getNickname();
    }
}
