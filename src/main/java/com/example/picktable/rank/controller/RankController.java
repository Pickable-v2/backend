package com.example.picktable.rank.controller;

import com.example.picktable.rank.domain.dto.WeeklyFoodRankResponseDTO;
import com.example.picktable.rank.domain.dto.WeeklyFoodTypeRankResponseDTO;
import com.example.picktable.rank.service.RankService;
import com.example.picktable.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rank")
public class RankController {

    private final RankService rankService;

    @GetMapping("/update-weekly")
    public String updateWeeklyRankings() {
        rankService.updateWeeklyRankings();
        return "Weekly rankings updated and counts reset";
    }

    @GetMapping("/foodType")
    public ResponseEntity<WeeklyFoodTypeRankResponseDTO> getTypeRank() {
        WeeklyFoodTypeRankResponseDTO topRestaurants = rankService.getTop5RestaurantsByCount();
        return ResponseEntity.ok(topRestaurants);
    }
    
    @GetMapping("food")
    public ResponseEntity<WeeklyFoodRankResponseDTO> getFoodRank() {
        WeeklyFoodRankResponseDTO topFoods = rankService.getTop5FoodsByChat();
       return ResponseEntity.ok(topFoods);
    }

    @GetMapping("/foodType/testScheduled")
    public ResponseEntity<Void> testScheduled() {
        rankService.updateWeeklyRankings();
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/init")
    public void initData() {
        rankService.initData();
    }

    @PatchMapping("/initCountFoodType")
    public void initCount() {
        rankService.initCount();
    }

    @PatchMapping("/initCountFood")
    public void initFoodCount() {
        rankService.initFoodCount();
    }
}
