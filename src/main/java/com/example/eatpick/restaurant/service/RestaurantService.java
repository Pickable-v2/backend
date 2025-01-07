package com.example.eatpick.restaurant.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.eatpick.restaurant.domain.dto.RestaurantResponse;
import com.example.eatpick.restaurant.domain.entity.Restaurant;
import com.example.eatpick.restaurant.repository.RestaurantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Page<RestaurantResponse> searchByName(String name, Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findByNameContaining(name, pageable);

        return restaurants.map(RestaurantResponse::from);
    }

    public Page<RestaurantResponse> searchByMenus(String menus, Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findByMenusContaining(menus, pageable);

        return restaurants.map(RestaurantResponse::from);
    }
}
