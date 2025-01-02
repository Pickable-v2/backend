package com.example.eatpick.friendship.domain.entity;

import com.example.eatpick.friendship.domain.status.FriendshipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIENDSHIP_ID")
    private Long id;

    @Column(nullable = false)
    private String fromMemberId;

    @Column(nullable = false)
    private String toMemberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus friendshipStatus;
}
