package com.example.eatpick.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.auth.domain.dto.request.SignUpRequest;
import com.example.eatpick.auth.domain.dto.response.SignUpResponse;
import com.example.eatpick.auth.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(description = "회원가입")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = memberService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
