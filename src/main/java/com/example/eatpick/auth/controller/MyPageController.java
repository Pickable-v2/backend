package com.example.eatpick.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.auth.domain.dto.request.MyPageUpdateRequest;
import com.example.eatpick.auth.domain.dto.response.MyPageUpdateResponse;
import com.example.eatpick.auth.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController("/mypage")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "Member API")
public class MyPageController {
    private final MemberService memberService;

    @PostMapping("/update")
    @Operation(description = "회원정보 수정")
    public ResponseEntity<MyPageUpdateResponse> myPageUpdate(@RequestBody MyPageUpdateRequest request) {
        MyPageUpdateResponse response = memberService.myPageUpdate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
