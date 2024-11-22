package com.example.picktable.chat.controller;

import com.example.picktable.chat.domain.dto.MeetChatResponseDTO;
import com.example.picktable.chat.service.ChatService;
import com.example.picktable.chatRoom.service.ChatRoomService;
import com.example.picktable.chatRoom.domain.dto.RoomAndFriendsRequestDTO;
import com.example.picktable.meet.domain.dto.DepartureResponseDTO;
import com.example.picktable.meet.domain.dto.MeetRequestDTO;
import com.example.picktable.meet.domain.dto.MeetResponseDTO;
import com.example.picktable.meet.service.MeetService;
import com.example.picktable.vote.domain.dto.VoteIdRequestDTO;
import com.example.picktable.vote.domain.dto.VoteRequestDTO;
import com.example.picktable.vote.domain.dto.VoteResponseDTO;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.vote.domain.entity.Vote;
import com.example.picktable.chatRoom.repository.ChatRoomRepository;
import com.example.picktable.member.service.MemberService;
import com.example.picktable.meet.repository.MeetRepository;
import com.example.picktable.vote.repository.VoteRepository;
import com.example.picktable.vote.service.VoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Chat API")
public class ChatController {

    private final ChatService chatService;
    private final VoteService voteService;
    private final MeetService meetService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final MeetRepository meetRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final VoteRepository voteRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final List<String> departureList = new ArrayList<>();

    @MessageMapping("/invite")
    @Operation(description = "채팅 친구 초대")
    public void inviteFriendsToRoom(RoomAndFriendsRequestDTO requestDTO) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(requestDTO.getRoomId());
        Set<Member> friends = memberService.findAllByLoginIds(requestDTO.getFriendLoginIds());

        for (Member friend : friends) {
            messagingTemplate.convertAndSend("/topic/public/" + friend.getLoginId(), chatRoom.getId());
        }
    }

    @MessageMapping("/vote/register/{roomId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅 방 내 투표 생성")
    public VoteResponseDTO createVote(@DestinationVariable("roomId") Long roomId, VoteRequestDTO voteRequest) throws BadRequestException {
        try {
            Vote vote = voteService.createVote(voteRequest.getMenu1(), voteRequest.getMenu2());
            chatService.createVote(roomId, vote);

            return new VoteResponseDTO(vote.getId(), vote.getMenu1(), vote.getVoteCount1(), vote.getMenu2(), vote.getVoteCount2(), false); //,
        } catch (Exception e) {
            log.error("Error registering vote for roomId {}: {}", roomId, e.getMessage());
            throw new BadRequestException("Failed to register vote");
        }
    }

    @MessageMapping("/vote/increment/{roomId}/{voteId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅 방 내 투표 수 증가")
    public VoteResponseDTO incrementVote(@DestinationVariable("roomId") Long roomId, @DestinationVariable("voteId") Long voteId, VoteIdRequestDTO voteRequest) throws BadRequestException {
        try {
            Vote vote = voteService.getVote(voteId);

            if (vote.getVoteCount1() == null) {
                vote.setVoteCount1(0L);
            }
            if (vote.getVoteCount2() == null) {
                vote.setVoteCount2(0L);
            }

            vote.incrementVoteCount1(voteRequest.getVoteCount1());
            vote.incrementVoteCount2(voteRequest.getVoteCount2());

            voteRepository.save(vote);

            int memberCount = chatService.getMemberCount(roomId);
            long totalCount = vote.getVoteCount1() + vote.getVoteCount2();
            boolean isCountSame = memberCount == totalCount;

            return new VoteResponseDTO(vote.getId(), vote.getMenu1(), vote.getVoteCount1(), vote.getMenu2(), vote.getVoteCount2(), isCountSame);
        } catch (Exception e) {
            log.error("Error incrementing vote for voteId {}: {}", voteId, e.getMessage());
            throw new BadRequestException("Failed to increment vote count");
        }
    }

    @MessageMapping("/vote/end/{roomId}/{voteId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅방 내 메뉴 투표 종료 및 메뉴 저장")
    public MeetResponseDTO endVoteAndSaveMenu(@DestinationVariable("voteId") Long voteId, @DestinationVariable("roomId") Long roomId) throws BadRequestException {
            String maxVotedMenu = voteService.getMostVotedMenu(voteId);
            return meetService.registerMeetMenu(maxVotedMenu, roomId);
    }

    @MessageMapping("/meet/state/{roomId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅방 내 약속 종료")
    public ResponseEntity<?> findMeet(@PathVariable(name = "roomId", required = false) Long roomId) {
        MeetResponseDTO meet = chatService.findMeetById(roomId);
        return new ResponseEntity<>(meet, HttpStatus.OK);
    }

    @MessageMapping("/meet/register/{roomId}/{meetId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅방 내 약속 생성")
    public MeetChatResponseDTO createMeet(@DestinationVariable("roomId") Long roomId, @DestinationVariable("meetId") Long meetId, MeetRequestDTO meetRequestDTO) throws BadRequestException {
        Meet meet = meetService.findByMeetId(meetId);
        meet.setMeetLocate(meetRequestDTO.getMeetLocate());
        meet.setMeetTime(meetRequestDTO.getMeetTime());

        meetRepository.save(meet);

        return MeetChatResponseDTO.builder()
                .roomId(roomId)
                .meetId(meet.getId())
                .meetMenu(meet.getMeetMenu())
                .meetLocate(meet.getMeetLocate())
                .meetTime(meet.getMeetTime())
                .build();
    }

    @MessageMapping("/meet/end/{meetId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅방 내 약속 종료")
    public void endMeet(@DestinationVariable("meetId") Long meetId) throws BadRequestException {
        chatService.endMeet(meetId);
    }

    @MessageMapping("/departure/register/{roomId}")
    @SendTo("/topic/room/{roomId}")
    @Operation(description = "채팅방 내 출발지 등록")
    public ResponseEntity<DepartureResponseDTO> createDeparture(@DestinationVariable("roomId") Long roomId, String departures) throws BadRequestException {
        ChatRoom room = chatRoomRepository.findOneById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다.");
        }
        departureList.add(departures);

        String meetMenu = room.getMeet().getMeetMenu();
        String meetDate = room.getMeet().getMeetTime();
        if (meetMenu == null) {
            throw new IllegalArgumentException("해당 채팅방에 대한 Chat 정보가 없습니다.");
        }
        int memberCount = chatService.getMemberCount(roomId);

        DepartureResponseDTO responseDTO = DepartureResponseDTO.builder()
                .memberCount(memberCount)
                .meetMenu(meetMenu)
                .meetDate(meetDate)
                .departureList(departureList)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
