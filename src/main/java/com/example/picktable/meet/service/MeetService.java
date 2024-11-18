package com.example.picktable.meet.service;

import com.example.picktable.meet.domain.dto.MeetResponseDTO;
import com.example.picktable.chat.domain.entity.Chat;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.food.domain.entity.Food;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.chat.repository.ChatRepository;
import com.example.picktable.chatRoom.repository.ChatRoomRepository;
import com.example.picktable.food.repository.FoodRepository;
import com.example.picktable.food.service.FoodService;
import com.example.picktable.meet.repository.MeetRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MeetService {
    private final FoodService foodService;
    private final ChatRoomRepository chatRoomRepository;
    private final MeetRepository meetRepository;
    private final ChatRepository chatRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public MeetResponseDTO registerMeetMenu(String maxVotedMenu, Long chatRoomId) throws BadRequestException {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 채팅방입니다."));

        Optional<Meet> existingMeet = meetRepository.findByIdAndMeetMenu(chatRoom.getId(), maxVotedMenu);
        if (existingMeet.isPresent()) {
            log.info("해당 메뉴가 이미 채팅방에 등록되어 있습니다. 기존 메뉴를 반환합니다.");
            Meet meet = existingMeet.get();
            return MeetResponseDTO.builder()
                    .meetId(meet.getId())
                    .maxVotedMenu(maxVotedMenu)
                    .build();
        }

        Meet meet = new Meet();
        meet.setMeetMenu(maxVotedMenu);
        meet = meetRepository.saveAndFlush(meet);
        log.info("새로운 meet 객체가 저장되었습니다. meetId: {}", meet.getId());

        Chat chat = Chat.createChat(chatRoom, null, meet, SecurityUtil.getLoginId());
        chatRepository.save(chat);
        log.info("새로운 chat 객체가 저장되었습니다. chatId: {}", chat.getId());

        chatRoom.addMeet(meet);

        Food findFood = foodService.findByFoodName(maxVotedMenu)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 음식입니다."));
        findFood.incrementFoodCount();
        foodRepository.save(findFood);
        log.info("Food 객체가 업데이트되었습니다. foodName: {}, count: {}", findFood.getFoodName(), findFood.getCount());

        return MeetResponseDTO.builder()
                .meetId(meet.getId())
                .maxVotedMenu(maxVotedMenu)
                .build();
    }

    public Meet findByMeetId(Long meetId) throws BadRequestException {
        return meetRepository.findById(meetId).orElseThrow(() -> new BadRequestException("존재하지 않는 채팅방입니다."));
    }

    public Meet save(Meet meet) {
        return meetRepository.save(meet);
    }
}
