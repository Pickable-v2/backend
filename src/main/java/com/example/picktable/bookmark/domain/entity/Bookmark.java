package com.example.picktable.bookmark.domain.entity;

import jakarta.persistence.*;

import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID")
    private Restaurant restaurant;

    @Builder
    public Bookmark(Member member, Restaurant restaurant) {
        this.member = member;
        this.restaurant = restaurant;
    }
}
