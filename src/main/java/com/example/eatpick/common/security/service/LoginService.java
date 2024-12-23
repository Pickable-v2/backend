package com.example.eatpick.common.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // TODO: ErrorType 따로 관리
        Member member = memberService.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        return User.builder()
            .username(member.getLoginId())
            .password(member.getLoginPw())
            .roles(member.getRole().name())
            .build();
    }
}
