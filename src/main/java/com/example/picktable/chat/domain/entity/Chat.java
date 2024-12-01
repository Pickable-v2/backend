package com.example.picktable.chat.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.picktable.chatRoom.domain.entity.ChatRoom;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.notice.domain.entity.Notice;
import com.example.picktable.vote.domain.entity.Vote;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ID")
    private Long id;

    private String sender;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private ChatRoom room;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @PrePersist
    protected void onCreate() {
        sendDate = LocalDateTime.now();
    }

    @Builder
    public Chat(ChatRoom room, Vote vote, Meet meet, Notice notice, String sender) {
        this.room = room;
        this.vote = vote;
        this.meet = meet;
        this.notice = notice;
        this.sender = sender;
    }

    public static Chat createChat(ChatRoom room, Vote vote, Meet meet, String sender) {
        Chat chat = new Chat();
        chat.room = room;
        chat.vote = vote;
        chat.meet = meet;
        chat.sender = sender;
        chat.sendDate = LocalDateTime.now();
        return chat;
    }
}
