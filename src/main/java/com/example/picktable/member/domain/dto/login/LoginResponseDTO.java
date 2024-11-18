package com.example.picktable.member.domain.dto.login;

import com.example.picktable.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDTO {

        private final String loginId;
        private final String loginPw;

    @Builder
    public LoginResponseDTO(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }

    /* Entity -> DTO */
    public LoginResponseDTO(Member member) {
        this.loginId = member.getLoginId();
        this.loginPw = member.getLoginPw();
    }
}
