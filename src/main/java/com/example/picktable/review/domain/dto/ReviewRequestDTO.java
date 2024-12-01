package com.example.picktable.review.domain.dto;

import com.example.picktable.review.domain.entity.Review;
import com.example.picktable.review.domain.type.ReviewType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDTO {
    private long restaurant_id;
    private Long totalLikes;
    private int taste;
    private int mood;
    private int park;
    private int kind;
    private int cost;
    private ReviewType reviewType;
    private double stars;

    @Builder
    public ReviewRequestDTO(int taste, int mood, int park, int kind, int cost, ReviewType reviewType, double stars, Long totalLikes) {
        this.taste = taste;
        this.mood = mood;
        this.park = park;
        this.kind = kind;
        this.cost = cost;
        this.reviewType = reviewType;
        this.stars = stars;
        this.totalLikes = totalLikes;
    }

    public Review toEntity(){
            return Review.builder()
                    .taste(taste)
                    .cost(cost)
                    .mood(mood)
                    .kind(kind)
                    .park(park)
                    .reviewType(reviewType)
                    .stars(stars)
                    .totalLikes(totalLikes)
                    .build();
    }
}
