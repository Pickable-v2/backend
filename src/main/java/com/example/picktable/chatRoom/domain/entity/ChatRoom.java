package com.example.picktable.chatRoom.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.picktable.chatRoomMember.domain.entity.ChatRoomMember;
import com.example.picktable.meet.domain.entity.Meet;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.vote.domain.entity.Vote;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String hostId;
    private String roomName;
    private int currentUserNum;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "VOTE_ID")
    private Vote vote;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEET_ID")
    @JsonIgnore
    private Meet meet;

    public void addVote(Vote vote) {
        this.vote = vote;
        vote.setRoom(this);
    }

    public void addMeet(Meet meet) {
        this.meet = meet;
    }

    @Builder
    public ChatRoom(String hostId, String roomName, int currentUserNum) {
        this.hostId = hostId;
        this.roomName = roomName;
        this.currentUserNum = currentUserNum;
    }

    public static ChatRoom createRoom(String hostId, String roomName) {
        return ChatRoom.builder()
                .hostId(hostId)
                .roomName(roomName)
                .build();
    }

    public void addParticipant(Member member) {
        if (chatRoomMembers.stream().noneMatch(chatRoomMember -> chatRoomMember.getMember().equals(member))) {
            ChatRoomMember chatRoomMember = new ChatRoomMember(this, member);
            this.chatRoomMembers.add(chatRoomMember);
            member.getChatRoomMembers().add(chatRoomMember);
            this.currentUserNum += 1;
        }
    }
}
