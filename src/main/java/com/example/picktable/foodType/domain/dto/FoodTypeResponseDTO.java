package com.example.picktable.foodType.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodTypeResponseDTO {
    private Long id;
    private String foodTypeName;
    private Long count;
    private Long ranks;

    public FoodTypeResponseDTO(Long id, String foodTypeName, int count, int rank) {
        this.id=id;
        this.foodTypeName=foodTypeName;
        this.count= (long) count;
        this.ranks= (long) rank;
    }
}
