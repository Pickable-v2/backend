package com.example.picktable.friendship.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.friendship.domain.dto.FriendListDTO;
import com.example.picktable.friendship.domain.dto.FriendListResponseDTO;
import com.example.picktable.friendship.domain.dto.FriendStatusResponseDTO;
import com.example.picktable.friendship.domain.entity.Friendship;
import com.example.picktable.friendship.repository.FriendshipRepository;
import com.example.picktable.friendship.service.FriendshipService;
import com.example.picktable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Friendship", description = "Friendship API")
public class FriendshipController {
    private final MemberService memberService;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository;

    @GetMapping("/friend/search")
    @Operation(description = "친구 아이디 검색")
    public ResponseEntity<Page<FriendListResponseDTO>> searchFriend(@RequestParam(name = "loginId") String loginId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) throws Exception {
        if(!memberService.confirmId(loginId)) {
            throw new BadRequestException("존재하지 않는 사용자입니다.");
        }
        Page<FriendListResponseDTO> responseDTOS = memberService.searchByLoginId(loginId, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PostMapping("/friend/add/{loginId}")
    @Operation(description = "친구 추가")
    public ResponseEntity<MsgResponseDTO> addFriend(@Valid @PathVariable("loginId") String loginId) throws Exception {
        if(!memberService.confirmId(loginId)) {
            throw new BadRequestException("존재하지 않는 사용자입니다.");
        }
        friendshipService.createFriendship(loginId);

        return ResponseEntity.ok(new MsgResponseDTO("친구 추가 완료", HttpStatus.OK.value()));
    }

    @GetMapping("/friend-list")
    @Operation(description = "친구 목록 반환")
    public ResponseEntity<?> getFriendInfo() throws Exception {
        List<FriendListDTO> friendList = friendshipService.getFriendList();

        return new ResponseEntity<>(friendList, HttpStatus.OK);
    }

    @GetMapping("/friend-add-list")
    @Operation(description = "친구 추가 목록 반환")
    public ResponseEntity<?> getWaitingFriendInfo() throws Exception {
        List<FriendListDTO> waitingFriendList = friendshipService.getWaitingFriendList();

        return new ResponseEntity<>(waitingFriendList, HttpStatus.OK);
    }

    @PostMapping("/friend/accept/{friendshipId}")
    @Operation(description = "친구 추가 수락")
    public ResponseEntity<?> acceptFriend(@Valid @PathVariable("friendshipId") Long friendshipId) throws Exception {
        Friendship byId = friendshipRepository.findById(friendshipId).orElseThrow();
        friendshipService.acceptFriendRequest(friendshipId);

        FriendStatusResponseDTO responseDTO = FriendStatusResponseDTO.builder()
                .status(byId.getStatus()).build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/friend/cancel/{friendshipId}")
    @Operation(description = "친구 추가 취소")
    public ResponseEntity<?> cancelFriend(@Valid @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.cancelFriendRequest(friendshipId);
        return ResponseEntity.ok(new MsgResponseDTO("친구 추가 취소", HttpStatus.OK.value()));
    }
}
