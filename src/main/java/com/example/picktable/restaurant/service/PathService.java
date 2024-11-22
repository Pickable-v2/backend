package com.example.picktable.restaurant.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.picktable.restaurant.domain.dto.PersonalPathDTO;
import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import com.example.picktable.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathService {
    private final RestaurantRepository restaurantRepository;
    private final TMapService tmapService;
    private int serialNum = 0;

    @Transactional
    public List<PersonalPathDTO> getWeight(@Param("keyword") String keyword, @Param("startAddress") List<String> startAddress) {
        List<PersonalPathDTO> resultSort = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        String searchDttm = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        List<String> addressInfo = new ArrayList<>();
        Map<String, Double> coordinates;
        Double startX = 0.0;
        Double startY = 0.0;

        for (int i = 0; i < startAddress.size(); i++) {
            coordinates = tmapService.getCoordinates(startAddress.get(i));
            startX = coordinates.get("longitude");
            startY = coordinates.get("latitude");


            String userAddress = tmapService.getAddressByCoordinates(startX, startY);

            if (addressInfo.contains(userAddress)) {
                userAddress = tmapService.getAddressByCoordinates2(startX, startY);
            }
            addressInfo.add(userAddress);
        }

        for (String targetAddress : addressInfo) {
            List<PersonalPathDTO> pathList = personalRestaurant(keyword, targetAddress, searchDttm, startX, startY);
            pathList = setSerialNumForArray(pathList);
            resultSort.addAll(pathList);
        }

        for (String address : startAddress) {
            Map<String, Double> coordinates2 = tmapService.getCoordinates(address);
            Double startX2 = coordinates2.get("longitude");
            Double startY2 = coordinates2.get("latitude");
            List<PersonalPathDTO> pathList = setTotalTime(resultSort, startX2, startY2, searchDttm);
            pathList = sortByPath(pathList);
            for (int j = 0; j < pathList.size(); j++) {
                PersonalPathDTO target = pathList.get(j);
                target.setWeight(target.getWeight() + j); //TODO weight = 0 인 경우 확인
            }
        }
        resultSort = sortPersonalPathByWeightTop3(resultSort);
        for (PersonalPathDTO personalPathDTO : resultSort) {
            log.info("사용자 경로 반환 : serialNum = {}, weight = {}, totalTime = {}, routeInfo = {}",
                    personalPathDTO.getSerialNum(), personalPathDTO.getWeight(), personalPathDTO.getTotalTime(), personalPathDTO.getRouteInfo());
        }
        return resultSort;
    }

    public void changeSubArray(int start, int end, List<PersonalPathDTO> resultSort) {
        List<PersonalPathDTO> subArray = resultSort.subList(start, end + 1);
        Collections.sort(subArray, Comparator.comparingDouble((PersonalPathDTO dto) -> dto.getRestaurantResponseDTO().getDegree()).reversed());
    }

    public List<PersonalPathDTO> sortPersonalPathByWeightTop3(List<PersonalPathDTO> list) {
        Collections.sort(list, Comparator.comparingInt(PersonalPathDTO::getWeight));

        int start = -1;
        int end = -1;
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            if (Objects.equals(list.get(i).getWeight(), list.get(i + 1).getWeight())) {
                start = i;
                int j = i;
                while (Objects.equals(list.get(j).getWeight(), list.get(j + 1).getWeight())) {
                    j++;
                    if(j == size -1) {
                        break;
                    }
                }
                end = j;
                i = j;
                changeSubArray(start, end, list);
            }
        }
        return list.size() > 3 ? list.subList(0, 3) : list;
    }

    @Transactional
    public List<PersonalPathDTO> setTotalTime(List<PersonalPathDTO> list, Double startX, Double startY, String searchDttm) {
        List<PersonalPathDTO> results = new ArrayList<>();
        for (PersonalPathDTO target : list) {
            target.setTotalTime(tmapService.totalTime(Double.toString(startX), Double.toString(startY),
                    Double.toString(target.getRestaurantResponseDTO().getLongitude()), Double.toString(target.getRestaurantResponseDTO().getLatitude()), 0, "json", 1, searchDttm));
            results.add(target);
        }
        return results;
    }

    @Transactional
    public List<PersonalPathDTO> personalRestaurant(String keyword, String userAddress, String searchDttm, Double startX, Double startY) {
        List<Restaurant> restaurants = new ArrayList<>();
        List<PersonalPathDTO> personalPaths = new ArrayList<>();

        restaurants = restaurantRepository.findByOnlyAddress(userAddress);
        restaurants = filterByKeyword(restaurants, keyword);
        restaurants = sortByDegree(restaurants);
        personalPaths = getPath(startX, startY, restaurants, searchDttm);
        personalPaths = sortByPath(personalPaths);
        return personalPaths;
    }

    @Transactional
    public List<PersonalPathDTO> setSerialNumForArray(List<PersonalPathDTO> personalPaths) {
        for (PersonalPathDTO personalPath : personalPaths) {
            personalPath.setSerialNum(serialNum);
            serialNum++;
        }
        return personalPaths;
    }

    @Transactional
    public List<Restaurant> sortByDegree(List<Restaurant> restaurants) {
        return restaurants.stream()
                .sorted((r1, r2) -> Double.compare(r2.getDegree(), r1.getDegree()))
                .limit(20)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PersonalPathDTO> sortByPath(List<PersonalPathDTO> personalPaths) {
        return personalPaths.stream()
                .sorted((r1, r2) -> Double.compare(r1.getTotalTime(), r2.getTotalTime()))
                .limit(5)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PersonalPathDTO> getPath(Double startX, Double startY, List<Restaurant> restaurants, String searchDttm) {
        List<PersonalPathDTO> personalPaths = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            PersonalPathDTO target = new PersonalPathDTO();
            target.setTotalTime(tmapService.totalTime(
                    Double.toString(startX), Double.toString(startY),
                    Double.toString(restaurant.getLongitude()), Double.toString(restaurant.getLatitude()),
                    0, "json", 1, searchDttm));

            RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO(
                    restaurant
            );

            target.setRestaurantResponseDTO(restaurantResponseDTO);
            personalPaths.add(target);
        }
        return personalPaths;
    }

    private List<Restaurant> filterByKeyword(List<Restaurant> restaurants, String keyword) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getName().contains(keyword) ||
                        restaurant.getMenus().contains(keyword))
                .collect(Collectors.toList());
    }
}
