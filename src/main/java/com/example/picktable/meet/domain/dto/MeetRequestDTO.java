package com.example.picktable.meet.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetRequestDTO {
    private String meetLocate;
    private String meetTime;
}
