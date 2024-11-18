package com.example.picktable.likes.domain.dto;

import com.example.picktable.likes.domain.entity.Likes;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.review.domain.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LikesRequestDTO {
    private Boolean state;
    private Review review;

    @Builder
    public LikesRequestDTO(Review review,Boolean state){
        this.review=review;
        this.state=state;
    }
    public Likes toSaveEntity(Member member, Review review,Boolean state ){
        System.out.println("좋아요 테이블에 리뷰 저장");
        return Likes.builder()
                .member(member)
                .review(review)
                .state(state)
                .build();
    }
}
