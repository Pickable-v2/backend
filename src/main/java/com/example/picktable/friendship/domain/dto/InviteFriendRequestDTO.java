package com.example.picktable.friendship.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class InviteFriendRequestDTO {
    private List<String> friendLoginIds;
}
