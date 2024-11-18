package com.example.picktable.restaurant.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendedRestaurantsDTO {
    private String keyword;
    private List<String> startAddress;
    private String searchDttm;
}
