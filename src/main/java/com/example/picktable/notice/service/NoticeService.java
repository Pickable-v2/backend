package com.example.picktable.notice.service;

import com.example.picktable.notice.domain.dto.NoticeResponseDTO;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.notice.domain.entity.Notice;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public List<NoticeResponseDTO> getNoticesByMember(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Notice> notices = noticeRepository.findAllByMember(member);

        return notices.stream().map(notice -> new NoticeResponseDTO(
                notice.getId(),
                notice.getMember().getId(),
                notice.getContent(),
                notice.getNoticeType()
        )).collect(Collectors.toList());
    }
}
