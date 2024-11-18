package com.example.picktable.notice.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.chat.domain.entity.Chat;
import com.example.picktable.common.domain.entity.BaseTimeEntity;
import com.example.picktable.notice.domain.type.NoticeType;
import com.example.picktable.member.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long id;
    private String content;
    private NoticeType noticeType;

    @OneToOne(mappedBy = "notice", fetch = FetchType.LAZY)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Notice(Member member, String content,NoticeType noticeType) {
        this.member = member;
        this.content = content;
        this.noticeType=noticeType;
    }
}
