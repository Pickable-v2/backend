package com.example.picktable.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VoteChatResponseDTO {

    private Long roomId;
    private Long menu1Count;
    private Long menu2Count;
}
