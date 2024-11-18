package com.example.picktable.rank.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.common.domain.entity.BaseTimeEntity;
import com.example.picktable.food.domain.dto.FoodResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeeklyFoodRank extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_RANK_ID")
    private Long id;
    private String date;

    @Transient
    private List<FoodResponseDTO> foods = new ArrayList<>();
}
