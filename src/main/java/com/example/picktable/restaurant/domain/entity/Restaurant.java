package com.example.picktable.restaurant.domain.entity;

import com.example.picktable.foodType.domain.entity.FoodType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.example.picktable.bookmark.domain.entity.Bookmark;
import com.example.picktable.review.domain.entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESTAURANT_ID")
    private Long id;

    private String name;
    private String restaurantType;
    private Double degree;
    private String addressRoad;
    private String addressNumber;
    private String tel;
    private String menus;

    private int totalReviews;
    private int totalTaste;
    private int totalCost;
    private int totalKind;
    private int totalMood;
    private int totalPark;

    private Double latitude;
    private Double longitude;

    private Integer pathTime;
    private Double distance;
    private Long count;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Bookmark> bookmark;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodType_id")
    private FoodType foodType;

    public void setCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void calculateDegree(Double newDegree) {
        if (totalReviews == 0) {
            degree = newDegree;
        } else {
            degree = ((degree * totalReviews) + newDegree) / (totalReviews + 1);
        }
        degree = Math.round(degree * 10) / 10.0;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", degree=" + degree +
                ", addressRoad='" + addressRoad + '\'' +
                ", addressNumber='" + addressNumber + '\'' +
                ", pathTime=" + pathTime +
                '}';
    }
}
