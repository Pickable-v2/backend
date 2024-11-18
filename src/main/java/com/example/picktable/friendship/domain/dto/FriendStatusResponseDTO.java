package com.example.picktable.friendship.domain.dto;

import com.example.picktable.friendship.domain.type.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendStatusResponseDTO {
    private FriendshipStatus status;

    @Builder
    public FriendStatusResponseDTO(FriendshipStatus status) {
        this.status = status;
    }
}
