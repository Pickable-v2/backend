package com.example.picktable.food.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodResponseDTO {
    private Long id;
    private String foodName;
    private Long count;
    private Long ranks;

    public FoodResponseDTO(Long id, String foodName, Long count, Long ranks) {
        this.id = id;
        this.foodName = foodName;
        this.count = count;
        this.ranks=ranks;
    }
}
