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

    private final RestaurantService restaurantService;
    private final RankService rankService;

    // -> 순위초기화
    @GetMapping("/update-weekly")
    public String updateWeeklyRankings() {
        rankService.updateWeeklyRankings();
        return "Weekly rankings updated and counts reset";
    }


    //주간 음식종류별 순위
    @GetMapping("/foodType")
    public ResponseEntity<WeeklyFoodTypeRankResponseDTO> getTypeRank() {
        WeeklyFoodTypeRankResponseDTO topRestaurants = rankService.getTop5RestaurantsByCount();
        return ResponseEntity.ok(topRestaurants);
    }
    
    //주간 음식별 순위 -> 채팅방 투표결과 기준
    @GetMapping("food")
    public ResponseEntity<WeeklyFoodRankResponseDTO> getFoodRank() {
        WeeklyFoodRankResponseDTO topFoods = rankService.getTop5FoodsByChat();
       return ResponseEntity.ok(topFoods);
    }

    // 강제로 @Scheduled 메서드를 호출하는 테스트용 엔드포인트
    @GetMapping("/foodType/testScheduled")
    public ResponseEntity<Void> testScheduled() {
        rankService.updateWeeklyRankings();
        return ResponseEntity.ok().build();
    }

    //setFoodType
    @PatchMapping("/init")
    public void initData() {
        rankService.initData();
    }

    //restaurant의 foodType count 초기화
    @PatchMapping("/initCountFoodType")
    public void initCount() {
        rankService.initCount();
    }

    //food count 초기화
    @PatchMapping("/initCountFood")
    public void initFoodCount() {
        rankService.initFoodCount();
    }

}
