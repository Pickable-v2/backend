package com.example.eatpick.auth.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eatpick.auth.domain.dto.request.SignInRequest;
import com.example.eatpick.auth.domain.dto.request.SignUpRequest;
import com.example.eatpick.auth.domain.dto.response.SignInResponse;
import com.example.eatpick.auth.domain.dto.response.SignUpResponse;
import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.repository.MemberRepository;
import com.example.eatpick.common.security.domain.dto.JwtToken;
import com.example.eatpick.common.security.service.JwtTokenProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(SignUpRequest request) {
        Member member = request.toEntity();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

        return new SignUpResponse(member);
    }

    public SignInResponse signIn(SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            request.getLoginId(), request.getLoginPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return new SignInResponse(request.getLoginId(), request.getLoginPw(), jwtToken);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}
