package com.example.eatpick.auth.service;

import org.springframework.stereotype.Service;

import com.example.eatpick.auth.domain.dto.request.SignUpRequest;
import com.example.eatpick.auth.domain.dto.response.SignUpResponse;
import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        Member member = request.toEntity();
        memberRepository.save(member);

        return new SignUpResponse(member);
    }
}
