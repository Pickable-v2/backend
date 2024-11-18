package com.example.picktable.likes.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.review.domain.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKES_ID")
    private Long id;
    private Boolean state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Likes(Member member, Boolean state, Review review){
        this.member=member;
        this.state=state;
        this.review=review;
    }
}
