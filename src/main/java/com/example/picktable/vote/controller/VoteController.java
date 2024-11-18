package com.example.picktable.vote.controller;

import com.example.picktable.meet.domain.dto.MeetResponseDTO;
import com.example.picktable.restaurant.domain.dto.PersonalPathDTO;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.chatRoom.repository.ChatRoomRepository;
import com.example.picktable.meet.service.MeetService;
import com.example.picktable.restaurant.service.PathService;
import com.example.picktable.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final MeetService meetService;

    @PostMapping("/vote/end/{roomId}/{voteId}")
    public MeetResponseDTO endVoteAndSaveMenu(@PathVariable("voteId") Long voteId, @PathVariable("roomId") Long roomId) throws BadRequestException {
        try {
            String maxVotedMenu = voteService.getMostVotedMenu(voteId);
            return meetService.registerMeetMenu(maxVotedMenu, roomId);
        } catch (Exception e) {
            log.error("Error ending vote for voteId {}: {}", voteId, e.getMessage());
            throw new BadRequestException("Failed to end vote and save menu", e);
        }
    }
}
