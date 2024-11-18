package com.example.picktable.friendship.domain.dto;

import com.example.picktable.friendship.domain.type.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendListDTO {

     private Long friendshipId;
     private String friendLoginId;
     private String friendNickname;
     private FriendshipStatus status;
     private String imgUrl;
}
