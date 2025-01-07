package com.example.eatpick.restaurant.domain.dto;

import com.example.eatpick.restaurant.domain.entity.Restaurant;
import com.example.eatpick.restaurant.domain.type.RestaurantType;

public record RestaurantResponse(
    Long id,
    String name,
    String telNumber,
    String menus,
    RestaurantType restaurantType
) {

    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getTelNumber(), restaurant.getMenus(), restaurant.getRestaurantType());
    }
}
