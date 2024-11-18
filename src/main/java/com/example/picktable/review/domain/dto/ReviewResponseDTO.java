package com.example.picktable.review.domain.dto;

import com.example.picktable.review.domain.entity.Review;
import com.example.picktable.review.domain.type.ReviewType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long id;
    private Long totalLikes;
    private String writers;
    private int taste;
    private int cost;
    private int kind;
    private int mood;
    private int park;
    private String createdDate;
    private ReviewType reviewType;
    private double stars;
    private Long member_id;

    public ReviewResponseDTO(Review review) {
        if (review.getMember() != null) {
            this.member_id = review.getMember().getId();
        } else {
            this.member_id = null;
        }
        if (review.getReviewType() != null) {
            this.reviewType = review.getReviewType();
        } else {
            this.reviewType = ReviewType.NOT_CERTIFY;
        }
        this.id = review.getId();
        this.writers = review.getWriter();
        this.cost = review.getCost();
        this.park = review.getPark();
        this.mood = review.getMood();
        this.kind = review.getKind();
        this.taste = review.getTaste();
        this.totalLikes = review.getTotalLikes();
        this.createdDate = review.getCreatedDate();
        this.stars = review.getStars();
    }
}
