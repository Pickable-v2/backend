package com.example.eatpick.friendship.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.auth.service.MemberService;
import com.example.eatpick.friendship.domain.dto.FriendshipResponse;
import com.example.eatpick.friendship.domain.dto.SearchResponse;
import com.example.eatpick.friendship.domain.entity.Friendship;
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
    public ResponseEntity<Page<SearchResponse>> search(@RequestParam String loginId,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        if (!memberService.checkLoginId(loginId)) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
        Page<SearchResponse> responseDTOS = memberService.searchByLoginId(loginId, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(description = "친구 추가")
    public ResponseEntity<FriendshipResponse> addFriend(@Valid @RequestParam String loginId) throws
        BadRequestException {
        FriendshipResponse response = friendshipService.createFriendship(loginId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept")
    @Operation(description = "친구 추가 수락")
    public ResponseEntity<FriendshipResponse> acceptFriend(@Valid @RequestParam Long friendshipId) throws Exception {
        FriendshipResponse response = friendshipService.acceptFriendRequest(friendshipId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cancel")
    @Operation(description = "친구 추가 취소")
    public ResponseEntity<FriendshipResponse> cancelFriend(@Valid @RequestParam Long friendshipId) {
        friendshipService.cancelFriendRequest(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/friend-list")
    @Operation(description = "친구 목록 조회")
    public ResponseEntity<?> getFriendInfo() throws Exception {
        List<FriendshipResponse> friends = friendshipService.getFriends();

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friend-add-list")
    @Operation(description = "친구 추가 목록 조회")
    public ResponseEntity<?> getWaitingFriendInfo() throws Exception {
        List<FriendshipResponse> waitingFriends = friendshipService.getWaitingFriends();

        return new ResponseEntity<>(waitingFriends, HttpStatus.OK);
    }
}
