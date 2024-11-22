package com.example.picktable.rank.controller;

import com.example.picktable.rank.domain.dto.WeeklyFoodRankResponseDTO;
import com.example.picktable.rank.domain.dto.WeeklyFoodTypeRankResponseDTO;
import com.example.picktable.rank.service.RankService;
import com.example.picktable.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rank")
@Tag(name = "Rank", description = "Rank API")
public class RankController {
    private final RankService rankService;

    @GetMapping("/update-weekly")
    @Operation(description = "주간 순위 업데이트")
    public String updateWeeklyRankings() {
        rankService.updateWeeklyRankings();
        return "Weekly rankings updated and counts reset";
    }

    @GetMapping("/foodType")
    @Operation(description = "음식 종류별 랭킹 반환")
    public ResponseEntity<WeeklyFoodTypeRankResponseDTO> getTypeRank() {
        WeeklyFoodTypeRankResponseDTO topRestaurants = rankService.getTop5RestaurantsByCount();
        return ResponseEntity.ok(topRestaurants);
    }
    
    @GetMapping("/food")
    @Operation(description = "음식별 랭킹 반환")
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
