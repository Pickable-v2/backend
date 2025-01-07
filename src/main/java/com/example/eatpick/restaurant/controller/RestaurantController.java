package com.example.eatpick.restaurant.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eatpick.restaurant.domain.dto.RestaurantResponse;
import com.example.eatpick.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Restaurant API")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Operation(description = "이름 검색")
    @GetMapping("/search-by-name")
    public ResponseEntity<Page<RestaurantResponse>> searchByName(
        @RequestParam(required = false) String name,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RestaurantResponse> response = restaurantService.searchByName(name, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "메뉴 검색")
    @GetMapping("/search-by-menus")
    public ResponseEntity<Page<RestaurantResponse>> searchByMenus(
        @RequestParam(required = false) String menus,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RestaurantResponse> response = restaurantService.searchByMenus(menus, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
