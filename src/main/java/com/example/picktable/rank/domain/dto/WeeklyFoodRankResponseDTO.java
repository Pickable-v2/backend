package com.example.picktable.rank.domain.dto;

import com.example.picktable.food.domain.dto.FoodResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WeeklyFoodRankResponseDTO {
    private Long id;
    private String date;
    private List<FoodResponseDTO> topFoods;

    public WeeklyFoodRankResponseDTO(Long id,String date, List<FoodResponseDTO> topFoods) {
        this.id=id;
        this.date = date;
        this.topFoods = topFoods;
    }
}
