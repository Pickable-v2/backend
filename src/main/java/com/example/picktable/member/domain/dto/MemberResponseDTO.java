package com.example.picktable.member.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDTO {
    private Long id;
    private String loginId;
    private String loginPw;
    private String nickname;
    private String gender;
    private int age;
}
