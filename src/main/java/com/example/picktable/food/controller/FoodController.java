package com.example.picktable.food.controller;

import com.example.picktable.food.domain.entity.Food;
import com.example.picktable.food.service.FoodService;
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
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/main/image")
    public String getMainImage(@RequestParam String foodName) throws BadRequestException {
        return foodService.getImageRouteByFoodName(foodName);
    }

    @PatchMapping("/update-image-routes")
    public ResponseEntity<String> updateImageRoutes() {
        foodService.updateImageRoutes();
        return new ResponseEntity<>("Image routes updated successfully", HttpStatus.OK);
    }

    @GetMapping("/image/{foodName}")
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
