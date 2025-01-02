package com.example.eatpick.friendship.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.auth.service.MemberService;
import com.example.eatpick.friendship.domain.dto.FriendshipResponse;
import com.example.eatpick.friendship.domain.dto.SearchResponse;
import com.example.eatpick.friendship.service.FriendshipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
@Tag(name = "Friendship", description = "Friendship API")
public class FriendshipController {
    private final MemberService memberService;
    private final FriendshipService friendshipService;

    @GetMapping("/search")
    @Operation(description = "친구 아이디 검색")
    public ResponseEntity<Page<SearchResponse>> search(@RequestParam(name = "loginId") String loginId,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        if (!memberService.checkLoginId(loginId)) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
        Page<SearchResponse> responseDTOS = memberService.searchByLoginId(loginId, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PostMapping("/friend/add")
    @Operation(description = "친구 추가")
    public ResponseEntity<FriendshipResponse> addFriend(@Valid @RequestParam("loginId") String loginId) throws
        BadRequestException {
        FriendshipResponse response = friendshipService.createFriendship(loginId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
