package com.example.eatpick.friendship.domain.entity;

import com.example.eatpick.friendship.domain.status.FriendshipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "friendship",
    indexes = {
        @Index(name = "idx_from_to_member", columnList = "fromMemberId, toMemberId")
    }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIENDSHIP_ID")
    private Long id;

    @Column(nullable = false)
    private Long fromMemberId;

    @Column(nullable = false)
    private Long toMemberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus friendshipStatus;

    public static Friendship of(Long fromMemberId, Long toMemberId, FriendshipStatus friendshipStatus) {
        Long user1 = Math.min(fromMemberId, toMemberId);
        Long user2 = Math.max(fromMemberId, toMemberId);
        return new Friendship(null, user1, user2, friendshipStatus);
    }
}
