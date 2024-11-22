package com.example.picktable.chat.service;

import com.example.picktable.meet.domain.dto.MeetResponseDTO;
import com.example.picktable.chat.domain.entity.Chat;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.vote.domain.entity.Vote;
import com.example.picktable.chat.repository.ChatRepository;
import com.example.picktable.chatRoom.repository.ChatRoomRepository;
import com.example.picktable.meet.repository.MeetRepository;
import com.example.picktable.vote.repository.VoteRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import com.example.picktable.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {
    private final FoodService foodService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MeetRepository meetRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public MeetResponseDTO findMeetById(Long roomId) {
        log.info("채팅룸 메시지 조회를 시작합니다. [roomId : {}]", roomId);
        Chat chat = chatRepository.findOneByRoomId(roomId);
        Meet meet = chat.getMeet();
        if (meet == null) {
            throw new IllegalArgumentException("해당 채팅방에 대한 Meet 정보가 없습니다.");
        }
        return MeetResponseDTO.builder()
                .meetId(meet.getId())
                .maxVotedMenu(meet.getMeetMenu())
                .build();
    }

    public void createVote(Long roomId, Vote vote) throws BadRequestException {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 채팅방입니다."));
        chatRoom.addVote(vote);
        voteRepository.save(vote);
        vote = voteRepository.findById(vote.getId()).orElseThrow(() -> new BadRequestException("Vote not found"));
        chatRoomRepository.save(chatRoom);
        chatRepository.save(Chat.createChat(chatRoom, vote, null, SecurityUtil.getLoginId()));
    }


    public int getMemberCount(Long chatRoomId) throws BadRequestException {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 채팅방입니다."));
        return chatRoom.getCurrentUserNum();
    }

    public Meet updateMeet(Long meetId, String meetLocate, String meetTime) throws BadRequestException {
        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new BadRequestException("존재하지 않는 약속입니다."));
        meet.updateMeet(meetLocate, meetTime);

        foodService.increaseFoodCount(meetLocate);

        return meetRepository.save(meet);
    }

    public void endMeet(Long meetId) throws BadRequestException {
        Meet meet = meetRepository.findById(meetId).orElseThrow(() -> new BadRequestException("존재하지 않는 약속입니다."));
        meetRepository.save(meet);
    }
}
