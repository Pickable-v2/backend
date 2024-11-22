package com.example.picktable.meet.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.picktable.chat.domain.dto.MeetChatResponseDTO;
import com.example.picktable.meet.domain.dto.MeetRequestDTO;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.meet.repository.MeetRepository;
import com.example.picktable.meet.service.MeetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Tag(name = "Meet", description = "Meet API")
public class MeetController {
    private final MeetService meetService;
    private final MeetRepository meetRepository;

    @PostMapping("/meet/register/{roomId}/{meetId}")
    @Operation(description = "약속 등록")
    public MeetChatResponseDTO createMeet(@PathVariable("roomId") Long roomId, @PathVariable("meetId") Long meetId, @RequestBody MeetRequestDTO meetRequestDTO) throws BadRequestException {
        Meet meet = meetService.findByMeetId(meetId);
        meet.setMeetLocate(meetRequestDTO.getMeetLocate());
        meet.setMeetTime(meetRequestDTO.getMeetTime());

        meetRepository.save(meet);

        return MeetChatResponseDTO.builder()
                .roomId(roomId)
                .meetLocate(meet.getMeetLocate())
                .meetTime(meet.getMeetTime())
                .build();
    }
}
