package com.example.picktable.notice.domain.dto;

import com.example.picktable.notice.domain.type.NoticeType;
import lombok.Getter;

@Getter
public class NoticeResponseDTO {
    private Long id;
    private Long memberId;
    private String createdDate;
    private String content;
    private NoticeType noticeType;
    public NoticeResponseDTO(Long id, Long memberId, String content,NoticeType noticeType) {
        this.id = id;
        this.memberId = memberId;
        this.content = content;
        this.noticeType=noticeType;
    }
}
