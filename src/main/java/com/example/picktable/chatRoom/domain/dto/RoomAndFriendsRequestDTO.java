package com.example.picktable.chatRoom.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RoomAndFriendsRequestDTO {
    private Long roomId;
    private String name;
    private List<String> friendLoginIds;

    // Getter 및 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFriendLoginIds() {
        return friendLoginIds;
    }
}
