package com.example.picktable.restaurant.controller;

import com.fasterxml.jackson.databind.JsonNode;

import com.example.picktable.restaurant.domain.dto.PathRequestDTO;
import com.example.picktable.restaurant.domain.dto.PathResponseDTO;
import com.example.picktable.restaurant.domain.dto.PersonalPathDTO;
import com.example.picktable.restaurant.domain.dto.RecommendedRestaurantsDTO;
import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.restaurant.service.PathService;
import com.example.picktable.restaurant.service.RestaurantService;
import com.example.picktable.restaurant.service.TMapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
@Tag(name = "Restaurant", description = "Restaurant API")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final TMapService tmapService;
    private final PathService pathService;

    @GetMapping("/search")
    @Operation(description = "음식점 검색")
    public ResponseEntity<Page<RestaurantResponseDTO>> search(@RequestParam(name = "word", required = false) String word,
                                                              @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/details")
    @Operation(description = "음식점 상세 정보 조회")
    public ResponseEntity<RestaurantResponseDTO> showDetails(@PathVariable("restaurantId") Long id) {
        RestaurantResponseDTO dto = restaurantService.showDetails(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/review")
    @Operation(description = "음식점 상세 정보 조회(리뷰만 조회)")
    public ResponseEntity<RestaurantResponseDTO> showDetailsOnlyReviews(@PathVariable("restaurantId") Long id) {
        RestaurantResponseDTO dto = restaurantService.showDetailsOnlyReviews(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    @Operation(description = "음식점 전체 조회")
    public ResponseEntity<Page<RestaurantResponseDTO>> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        System.out.println("findAll 함수 들어옴");
        Page<RestaurantResponseDTO> page = restaurantService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/degree")
    @Operation(description = "Degree로 음식점 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> getRestaurantsByDegree(@RequestParam(name = "word", required = false) String word,
                                                                              @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/reviews")
    @Operation(description = "리뷰로부터 음식점 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> getRestaurantsByTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                    @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants")
    @Operation(description = "음식점만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurant(@RequestParam(name = "word", required = false) String word,
                                                                          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlycafes")
    @Operation(description = "카페만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafe(@RequestParam(name = "word", required = false) String word,
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/routes")
    @Operation(description = "경로로 음식점 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> getRestaurantsByRoutes(
            @RequestParam(name = "word", required = false) String word,
            @RequestParam(name = "startX") Float startX,
            @RequestParam(name = "startY") Float startY,
            @PageableDefault(sort = "distance", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("distance"));

        Page<RestaurantResponseDTO> page = restaurantService.findByDistances(word, startX, startY, sortedPageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PatchMapping("/api/initCoordinates")
    public String initCoordinates() {
        restaurantService.updateCoordinates();
        return null;
    }

    @GetMapping("/reverseGeo")
    public String getAddressByCoordinates(double startX, double startY) {

        return tmapService.getAddressByCoordinates(startX, startY);
    }

    @GetMapping("/search/onlycafes/reviews")
    @Operation(description = "리뷰로부터 카페만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafeForTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                   @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlycafes/degree")
    @Operation(description = "Degree로부터 카페만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafeForDegree(@RequestParam(name = "word", required = false) String word,
                                                                             @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants/reviews")
    @Operation(description = "리뷰로부터 음식점만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurantForTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                         @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants/degree")
    @Operation(description = "Degree로부터 음식점만 반환")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurantForDegree(@RequestParam(name = "word", required = false) String word,
                                                                                   @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/search/totalPath2")
    @Operation(description = "대중교통 경로 반환2")
    public ResponseEntity<PathResponseDTO> getTransitRoute2(
            @RequestBody PathRequestDTO totalTimeRequest) {
        String departure = totalTimeRequest.getDeparture();
        String destination = totalTimeRequest.getDestination();
        String searchDttm = totalTimeRequest.getSearchDttm();

        PathResponseDTO routeInfo = tmapService.getTransitRoute2(departure, destination, 0, "json", 1, searchDttm);

        if (routeInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(routeInfo);
    }

   @PostMapping("/search/getPath")
   @Operation(description = "대중교통 경로 반환")
    public ResponseEntity<JsonNode> getTransitRoute(@RequestBody PathRequestDTO totalTimeRequest) {
        String departure = totalTimeRequest.getDeparture();
       String destination = totalTimeRequest.getDestination();
       String searchDttm = totalTimeRequest.getSearchDttm();

       JsonNode routeInfo = tmapService.getJsonByTransitRoute(departure, destination, 0, "json", 1, searchDttm);
       if (routeInfo == null) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
       }
        return ResponseEntity.ok(routeInfo);
   }

    @PostMapping("/search/getWeight")
    @Operation(description = "가중치 정보 반환")
    public ResponseEntity<List<PersonalPathDTO>> getWeightInfo(@RequestBody RecommendedRestaurantsDTO request) {
        List<PersonalPathDTO> personalPathList = pathService.getWeight(request.getKeyword(), request.getStartAddress());
        return ResponseEntity.ok(personalPathList);
    }

    @PostMapping("/search/getWalkPath")
    @Operation(description = "도보 경로 반환")
    public ResponseEntity<JsonNode> getWalkRoute(@RequestBody PathRequestDTO totalTimeRequest) {
        String departure = totalTimeRequest.getDeparture();
        String destination = totalTimeRequest.getDestination();

        JsonNode routeInfo = tmapService.getJsonByWalkRoute(departure,destination);

        if (routeInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(routeInfo);

    }

    @GetMapping("/search/showAddress")
    @Operation(description = "주소 테스트")
    public ResponseEntity<String> addressTest() {
        String address = tmapService.getAddressByCoordinates2(128.3592031, 36.0888125);
       return ResponseEntity.ok(address);
    }
}
