package com.example.picktable.chatRoom.controller;

import com.example.picktable.chatRoom.domain.dto.ChatRoomDTO;
import com.example.picktable.chatRoom.domain.dto.RoomAndFriendsRequestDTO;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.chatRoom.repository.ChatRoomRepository;
import com.example.picktable.chatRoomMember.domain.entity.ChatRoomMember;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.member.security.util.SecurityUtil;
import com.example.picktable.chatRoom.service.ChatRoomService;
import com.example.picktable.member.service.MemberService;
import com.example.picktable.restaurant.domain.dto.PersonalPathDTO;
import com.example.picktable.restaurant.service.PathService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final MemberService memberService;
    private final PathService pathService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 생성 및 친구 초대
     * @param requestDTO
     */
    @PostMapping("/room/create")
    public ResponseEntity<ChatRoom> createRoomAndInviteFriends(@RequestBody RoomAndFriendsRequestDTO requestDTO) {
        try {
            Member creator = memberService.findByLoginId(SecurityUtil.getLoginId())
                    .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
            Set<Member> friends = memberService.findAllByLoginIds(requestDTO.getFriendLoginIds());

            if (friends.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            ChatRoom chatRoom = chatRoomService.createRoomAndInviteFriends(requestDTO.getName(), creator, friends);
            return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 내가 속한 채팅방 전체 조회
     */
    @GetMapping("/chat/rooms")
    public ResponseEntity<?> getChatRoom() {
        List<ChatRoom> chatRooms = chatRoomService.findAllRoom();
        List<ChatRoomDTO> includeChatRooms = new ArrayList<>();
        String loginId = SecurityUtil.getLoginId();
        log.info("현재 로그인된 사용자 ID: " + loginId);

        for (ChatRoom room : chatRooms) {
            Set<ChatRoomMember> participants = room.getChatRoomMembers();
            if (participants == null || participants.isEmpty()) {
                log.info("참여자 목록이 비어 있습니다. 채팅방 ID: " + room.getId());
            } else {
                log.info("채팅방 ID: " + room.getId() + ", 참여자 수: " + participants.size());
                for (ChatRoomMember participant : participants) {
                    log.info("참여자 ID: " + participant.getMember().getLoginId());
                    if (Objects.equals(participant.getMember().getLoginId(), loginId)) {
                        includeChatRooms.add(ChatRoomDTO.from(room, loginId));
                        log.info("추가된 채팅방 ID: " + room.getId());
                        break;
                    }
                }
            }
        }
        log.info("총 포함된 채팅방 수: " + includeChatRooms.size());
        return new ResponseEntity<>(includeChatRooms, HttpStatus.OK);
    }

    @PostMapping("/departure/register/{roomId}")
    public ResponseEntity<List<PersonalPathDTO>> registerDeparture(@PathVariable("roomId") Long roomId, @RequestBody List<String> departures) {
        ChatRoom room = chatRoomRepository.findOneById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다.");
        }

        String meetMenu = room.getMeet().getMeetMenu();
        if (meetMenu == null) {
            throw new IllegalArgumentException("해당 채팅방에 대한 Chat 정보가 없습니다.");
        }
        List<PersonalPathDTO> weight = pathService.getWeight(meetMenu, departures);
        return new ResponseEntity<>(weight, HttpStatus.OK);
    }
}
