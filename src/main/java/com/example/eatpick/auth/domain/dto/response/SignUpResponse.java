package com.example.eatpick.auth.domain.dto.response;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;

import lombok.Getter;

@Getter
public class SignUpResponse {
    private Long id;
    private String loginId;
    private String loginPw;
    private String verifiedLoginPw;
    private String nickname;
    private GenderType gender;
    private RoleType role;
    private int age;

    public SignUpResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.loginPw = member.getLoginPw();
        this.verifiedLoginPw = member.getVerifiedLoginPw();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.role = member.getRole();
        this.age = member.getAge();
    }
}
