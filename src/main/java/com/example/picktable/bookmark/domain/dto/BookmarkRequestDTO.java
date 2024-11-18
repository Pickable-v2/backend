package com.example.picktable.bookmark.domain.dto;

import com.example.picktable.bookmark.domain.entity.Bookmark;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BookmarkRequestDTO {
    private Restaurant restaurant;

    public Bookmark toSaveEntity(Member member, Restaurant restaurant) {
        return Bookmark.builder()
                .member(member)
                .restaurant(restaurant)
                .build();
    }
}
