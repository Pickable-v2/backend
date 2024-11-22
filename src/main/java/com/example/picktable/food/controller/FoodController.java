package com.example.picktable.food.controller;

import com.example.picktable.food.domain.entity.Food;
import com.example.picktable.food.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Food", description = "Food API")
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/main/image")
    @Operation(description = "메인화면 이미지 경로 반환")
    public String getMainImage(@RequestParam String foodName) throws BadRequestException {
        return foodService.getImageRouteByFoodName(foodName);
    }

    @PatchMapping("/update-image-routes")
    @Operation(description = "메인화면 이미지 경로 수정")
    public ResponseEntity<String> updateImageRoutes() {
        foodService.updateImageRoutes();
        return new ResponseEntity<>("Image routes updated successfully", HttpStatus.OK);
    }

    @GetMapping("/image/{foodName}")
    @Operation(description = "메뉴 이름에 해당하는 이미지 반환")
    public ResponseEntity<InputStreamResource> getImageByFoodName(@PathVariable String foodName) {
        Optional<Food> foodOpt = foodService.findByFoodName(foodName);
        if (foodOpt.isPresent()) {
            Food food = foodOpt.get();
            String imageUrl = food.getImageRoute();

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamResource resource = new InputStreamResource(inputStream);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, connection.getContentType());

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
