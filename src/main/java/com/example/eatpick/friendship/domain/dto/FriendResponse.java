package com.example.eatpick.friendship.domain.dto;

import com.example.eatpick.friendship.domain.entity.Friendship;
import com.example.eatpick.friendship.domain.status.FriendshipStatus;

public record FriendResponse(
    Long id,
    String fromMemberId,
    String toMemberId,
    FriendshipStatus friendshipStatus
) {

    public static FriendResponse from(Friendship friendship) {
        return new FriendResponse(friendship.getId(), friendship.getFromMemberId(), friendship.getToMemberId(),
            friendship.getFriendshipStatus());
    }
}
