package com.example.picktable.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.example.picktable.common.domain.entity.BaseTimeEntity;
import com.example.picktable.review.domain.dto.ReviewRequestDTO;
import com.example.picktable.review.domain.type.ReviewType;
import com.example.picktable.likes.domain.entity.Likes;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;
    private Long totalLikes;
    private int taste;
    private int cost;
    private int kind;
    private int mood;
    private int park;
    private ReviewType reviewType = ReviewType.NOT_CERTIFY;
    private double stars;
    private String writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Likes> likesList;


    public void setTotalLikes(Long totalLikes) {
        this.totalLikes = totalLikes;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public void setWriter(String writer) {
        this.writer = writer;
    }
    public void setStars(double stars) {
        this.stars=stars;
    }

    public Review(ReviewRequestDTO requestDTO){
        this.taste = requestDTO.getTaste();
        this.cost = requestDTO.getCost();
        this.mood = requestDTO.getMood();
        this.kind = requestDTO.getKind();
        this.park = requestDTO.getPark();
        this.reviewType = requestDTO.getReviewType();
        this.stars = requestDTO.getStars();
        this.totalLikes = requestDTO.getTotalLikes();
    }

    public void confirmMember(Member member) {
        this.member = member;
        member.addReview(this);
    }

    // 리뷰 수정
    public void updateReview(int cost, int park, int mood, int kind, int taste, double stars) {
        this.cost = cost;
        this.park = park;
        this.mood= mood;
        this.kind = kind;
        this.taste = taste;
        this.stars = stars;
    }

}
