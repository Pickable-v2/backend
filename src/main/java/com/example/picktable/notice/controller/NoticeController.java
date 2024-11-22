package com.example.picktable.notice.controller;

import com.example.picktable.notice.domain.dto.NoticeResponseDTO;
import com.example.picktable.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notice", description = "Notice API")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notices/{loginId}")
    @Operation(description = "사용자의 알림 목록 반환")
    public List<NoticeResponseDTO> getNoticesByType(@PathVariable(name = "loginId") String loginId) {
        return noticeService.getNoticesByMember(loginId);
    }
}
