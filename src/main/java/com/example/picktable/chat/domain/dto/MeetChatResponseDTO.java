package com.example.picktable.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeetChatResponseDTO {
    private Long roomId;
    private Long meetId;
    private String meetLocate;
    private String meetMenu;
    private String meetTime;
}
