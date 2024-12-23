package com.example.eatpick.auth.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eatpick.auth.domain.dto.request.MemberRequest;
import com.example.eatpick.auth.domain.dto.request.MyPageUpdateRequest;
import com.example.eatpick.auth.domain.dto.request.SignInRequest;
import com.example.eatpick.auth.domain.dto.response.MemberResponse;
import com.example.eatpick.auth.domain.dto.response.MyPageUpdateResponse;
import com.example.eatpick.auth.domain.dto.response.SignInResponse;
import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.repository.MemberRepository;
import com.example.eatpick.common.security.domain.dto.JwtToken;
import com.example.eatpick.common.security.service.JwtTokenProvider;
import com.example.eatpick.common.security.util.SecurityUtil;

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

    public MemberResponse signUp(MemberRequest request) {
        Member member = request.toEntity();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

        return MemberResponse.from(member);
    }

    public SignInResponse signIn(SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            request.loginId(), request.loginPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return SignInResponse.of(request.loginId(), request.loginPw(), jwtToken);
    }

    public MyPageUpdateResponse myPageUpdate(MyPageUpdateRequest request) {
        Member member = findByLoginId(SecurityUtil.getLoginId()).orElseThrow(
            () -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        member.update(request);
        memberRepository.save(member);

        return MyPageUpdateResponse.from(member);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}
