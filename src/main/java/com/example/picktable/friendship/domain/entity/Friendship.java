package com.example.picktable.friendship.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.friendship.domain.type.FriendshipStatus;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberLoginId;
    private String friendLoginId;
    private FriendshipStatus status;
    private boolean isFrom;

    private Long counterpartId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void acceptFriendshipRequest() {
        status = FriendshipStatus.ACCEPT;
    }

    public void setCounterpartId(Long id) {
        counterpartId = id;
    }
}
