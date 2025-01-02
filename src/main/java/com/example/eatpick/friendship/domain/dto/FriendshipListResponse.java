package com.example.eatpick.friendship.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.eatpick.friendship.domain.entity.Friendship;
import com.example.eatpick.friendship.domain.status.FriendshipStatus;

public record FriendshipListResponse(
    Long id,
    Long fromMemberId,
    Long toMemberId,
    FriendshipStatus friendshipStatus
) {

    public static List<FriendshipResponse> from(List<Friendship> friendships) {
        return friendships.stream()
            .map(FriendshipResponse::from)
            .collect(Collectors.toList());
    }
}
