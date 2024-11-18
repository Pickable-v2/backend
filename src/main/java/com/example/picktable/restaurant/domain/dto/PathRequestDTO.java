package com.example.picktable.restaurant.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PathRequestDTO {
    private String departure;
    private String destination;
    private List<String> destinations;
    private String searchDttm;
}
