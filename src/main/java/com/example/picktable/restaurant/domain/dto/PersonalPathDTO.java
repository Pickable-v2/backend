package com.example.picktable.restaurant.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonalPathDTO {

    private RestaurantResponseDTO restaurantResponseDTO;
    private Integer totalTime;
    private Integer serialNum;
    private Integer weight = 0;
    private JsonNode routeInfo;

    public PersonalPathDTO(RestaurantResponseDTO restaurant, Integer totalTime, Integer serialNum) {
        this.restaurantResponseDTO = restaurant;
        this.totalTime = totalTime;
        this.serialNum = serialNum;
    }

    @Override
    public String toString() {
        return "PersonalPath{" +
                "restaurant=" + restaurantResponseDTO +
                ", totalTime=" + totalTime +
                ", serialNum=" + serialNum +
                ", weight=" + weight +
                '}';
    }
}
