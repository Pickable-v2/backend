package com.example.eatpick.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.auth.domain.dto.request.MemberRequest;
import com.example.eatpick.auth.domain.dto.request.SignInRequest;
import com.example.eatpick.auth.domain.dto.response.MemberResponse;
import com.example.eatpick.auth.domain.dto.response.SignInResponse;
import com.example.eatpick.auth.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(description = "회원가입")
    public ResponseEntity<MemberResponse> signUp(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    @Operation(description = "회원탈퇴")
    public ResponseEntity<MemberResponse> withdraw() {
        memberService.withdraw();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    @Operation(description = "로그인")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = memberService.signIn(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/check-loginId")
    @Operation(description = "아이디 중복 확인")
    public ResponseEntity<?> checkLoginId(@RequestParam String loginId) {
        boolean isLoginIdDuplicate = memberService.checkLoginId(loginId);

        if (isLoginIdDuplicate) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/check-nickname")
    @Operation(description = "닉네임 중복 확인")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isNicknameDuplicate = memberService.checkNickname(nickname);

        if (isNicknameDuplicate) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/check-password")
    @Operation(description = "비밀번호 확인")
    public ResponseEntity<?> checkPassword(@RequestParam String loginPw, @RequestParam String verifiedLoginPw) {
        boolean isPasswordCorrect = memberService.checkPassword(loginPw, verifiedLoginPw);

        if (!isPasswordCorrect) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
