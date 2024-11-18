package com.example.picktable.notice.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequestDTO {
    private String loginId;
    private String content;
    private String createdDate;
}
