package com.example.eatpick.friendship.domain.dto;

import com.example.eatpick.friendship.domain.entity.Friendship;
import com.example.eatpick.friendship.domain.status.FriendshipStatus;

public record FriendshipResponse(
    Long id,
    Long fromMemberId,
    Long toMemberId,
    FriendshipStatus friendshipStatus
) {

    public static FriendshipResponse from(Friendship friendship) {
        return new FriendshipResponse(friendship.getId(), friendship.getFromMemberId(), friendship.getToMemberId(),
            friendship.getFriendshipStatus());
    }
}
