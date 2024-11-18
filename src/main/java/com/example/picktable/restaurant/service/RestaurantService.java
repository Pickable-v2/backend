package com.example.picktable.restaurant.service;


import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.review.domain.dto.ReviewResponseDTO;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import com.example.picktable.restaurant.repository.RestaurantRepository;
import com.example.picktable.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final TMapService tmapService;
    private final ReviewService reviewService;
    private final RestaurantRepository restaurantRepository;

    public boolean existsByName(String name) {
        return restaurantRepository.existsByName(name);
    }

    @Transactional
    public Page<RestaurantResponseDTO> searchRestaurants(String keyword, Pageable pageable) {
        Page<Restaurant> restaurantPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            restaurantPage = restaurantRepository.findAll(pageable);
        } else {
            restaurantPage = restaurantRepository.findByMenuContainingOrNameContaining(keyword, pageable);
        }

        Page<RestaurantResponseDTO> dtoPage = restaurantPage.map(restaurant -> {
            RestaurantResponseDTO dto = new RestaurantResponseDTO(restaurant);
            return dto;
        });
        return dtoPage;
    }

    @Transactional
    public Page<RestaurantResponseDTO> searchOnlyCafes(String keyword, Pageable pageable) {
        Page<Restaurant> cafes;

        if (keyword == null || keyword.trim().isEmpty()) {
            cafes = restaurantRepository.findAllCafes(pageable);
        } else {
            cafes = restaurantRepository.findOnlyCafes(keyword, pageable);
        }

        Page<RestaurantResponseDTO> dtoPage = cafes.map(restaurant -> {
            RestaurantResponseDTO dto = new RestaurantResponseDTO(restaurant);
            return dto;
        });
        return dtoPage;
    }


    @Transactional
    public Page<RestaurantResponseDTO> searchOnlyRestaurant(String keyword, Pageable pageable) {
        Page<Restaurant> restaurants;

        if (keyword == null || keyword.trim().isEmpty()) {
            restaurants = restaurantRepository.findAllRestaurant(pageable);
        } else {
            restaurants = restaurantRepository.findRestaurantsExcludingCafes(keyword, pageable);
        }

        Page<RestaurantResponseDTO> dtoPage = restaurants.map(restaurant -> {
            RestaurantResponseDTO dto = new RestaurantResponseDTO(restaurant);
            return dto;
        });
        return dtoPage;
    }

    @Transactional
    public Page<RestaurantResponseDTO> findAll(Pageable pageable) {

        Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);
        Page<RestaurantResponseDTO> dtoPage = restaurantPage.map(restaurant -> {
            RestaurantResponseDTO dto = new RestaurantResponseDTO(restaurant);
            return dto;
        });
        return dtoPage;

    }

    @Transactional
    //음식점 직선거리
    public Page<RestaurantResponseDTO> findByDistances(String keyword, Float startX, Float startY, Pageable pageable) {
        //TODO 기본적으로 페이지 1에 대해서 출력은 하되 페이지 2부터는 다른 함수로 빼두는 게 좋을 거 같고 dtos를 활용해서 페이지 정보만 가지고 사용하면 됨
        String dong = tmapService.getAddressByCoordinates(startX, startY);
        List<Restaurant> list = null;
        List<RestaurantResponseDTO> dtos = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            list = restaurantRepository.findAllAddress(dong);
        } else {
            list = restaurantRepository.findAllAddress(dong, keyword);
        }

        for (Restaurant target : list) {
            Double distance = tmapService.calculateDistance(startX, startY, target.getLongitude(), target.getLatitude());
            target.setDistance(distance);

            dtos.add(new RestaurantResponseDTO(target));
        }

        Collections.sort(dtos, Comparator.comparingDouble(RestaurantResponseDTO::getDistance));
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<RestaurantResponseDTO> sublist;

        if (start >= dtos.size()) {
            sublist = Collections.emptyList();
        } else {
            sublist = dtos.subList(start, end);
        }

        Page<RestaurantResponseDTO> dtoPage = new PageImpl<>(sublist, pageable, dtos.size());
        dtos.clear();
        return dtoPage;
    }


    @Transactional
    public void updateCoordinates() {
        List<Restaurant> list = restaurantRepository.findAll();
        for (Restaurant restaurant : list) {
            Map<String, Double> coordinates = tmapService.getCoordinates(restaurant.getAddressRoad());
            if (coordinates != null && !coordinates.isEmpty()) {
                restaurant.setCoordinates(coordinates.get("latitude"), coordinates.get("longitude"));
            }
        }
    }

    @Transactional
    public RestaurantResponseDTO showDetails(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(RuntimeException::new);
        Page<ReviewResponseDTO> reviewList = reviewService.showReviewsByRestaurant(id, Pageable.ofSize(5));
        return new RestaurantResponseDTO(
                restaurant,
                reviewList
        );
    }

    @Transactional
    public RestaurantResponseDTO showDetailsOnlyReviews(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(RuntimeException::new);
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getRestaurantType(),
                restaurant.getDegree(),
                restaurant.getAddressRoad(),
                restaurant.getAddressNumber(),
                restaurant.getTel(),
                restaurant.getMenus(),
                restaurant.getTotalReviews(),
                restaurant.getTotalTaste(),
                restaurant.getTotalCost(),
                restaurant.getTotalKind(),
                restaurant.getTotalMood(),
                restaurant.getTotalPark()
        );
    }
}
