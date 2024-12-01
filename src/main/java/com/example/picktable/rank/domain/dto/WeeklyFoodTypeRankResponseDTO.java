package com.example.picktable.rank.domain.dto;

import com.example.picktable.foodType.domain.dto.FoodTypeResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WeeklyFoodTypeRankResponseDTO {
    private Long id;
    private String date;
    private List<FoodTypeResponseDTO> topFoodTypes;

    public WeeklyFoodTypeRankResponseDTO(Long id, String date, List<FoodTypeResponseDTO> topFoodTypes) {
        this.id=id;
        this.date = date;
        this.topFoodTypes = topFoodTypes;
    }
}
