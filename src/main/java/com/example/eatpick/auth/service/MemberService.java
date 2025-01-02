package com.example.eatpick.auth.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.example.eatpick.friendship.domain.dto.SearchResponse;
import com.example.eatpick.memberPreferencesTaste.domain.entity.MemberPreferencesTaste;
import com.example.eatpick.preferencesTaste.domain.entity.PreferencesTaste;
import com.example.eatpick.preferencesTaste.domain.type.PreferencesType;
import com.example.eatpick.preferencesTaste.repository.PreferencesTasteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PreferencesTasteRepository preferencesTasteRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse signUp(MemberRequest request) {
        Member member = request.toEntity();
        member.encodePassword(passwordEncoder);

        if (request.preferences() != null) {
            for (PreferencesType preferencesType : request.preferences()) {
                PreferencesTaste preferencesTaste = preferencesTasteRepository.findByPreferencesType(preferencesType)
                    .orElseGet(() -> preferencesTasteRepository.save(new PreferencesTaste(null, preferencesType)));

                MemberPreferencesTaste memberPreferencesTaste = new MemberPreferencesTaste(null, member,
                    preferencesTaste);
                member.addPreferences(memberPreferencesTaste);
            }
        }
        memberRepository.save(member);

        return MemberResponse.from(member);
    }

    public void withdraw() {
        Member member = getLoginMember();

        memberRepository.delete(member);
    }

    public SignInResponse signIn(SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            request.loginId(), request.loginPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return SignInResponse.of(request.loginId(), request.loginPw(), jwtToken);
    }

    public MyPageUpdateResponse myPageUpdate(MyPageUpdateRequest request) {
        Member member = getLoginMember();

        member.update(request);
        memberRepository.save(member);

        return MyPageUpdateResponse.from(member);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public boolean checkLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public boolean checkPassword(String loginPw, String verifiedLoginPw) {
        return loginPw.equals(verifiedLoginPw);
    }

    public Page<SearchResponse> searchByLoginId(String loginId, Pageable pageable) {
        return memberRepository.findByLoginIdContaining(loginId, pageable);
    }

    public Member getLoginMember() {
        return findByLoginId(SecurityUtil.getLoginId()).orElseThrow(
            () -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
    }
}
