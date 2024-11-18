package com.example.picktable.rank.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.global.domain.entity.BaseTimeEntity;
import com.example.picktable.foodType.domain.dto.FoodTypeResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeeklyFoodTypeRank extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_TYPE_RANK_ID")
    private Long id;
    private String date;

    @Transient
    private List<FoodTypeResponseDTO> foodTypes = new ArrayList<>();
}
