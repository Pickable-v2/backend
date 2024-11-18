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
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final TMapService tmapService;
    private final PathService pathService;

    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantResponseDTO>> search(@RequestParam(name = "word", required = false) String word,
                                                              @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/details")
    public ResponseEntity<RestaurantResponseDTO> showDetails(@PathVariable("restaurantId") Long id) {
        RestaurantResponseDTO dto = restaurantService.showDetails(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/review")
    public ResponseEntity<RestaurantResponseDTO> showDetailsOnlyReviews(@PathVariable("restaurantId") Long id) {
        RestaurantResponseDTO dto = restaurantService.showDetailsOnlyReviews(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<RestaurantResponseDTO>> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        System.out.println("findAll 함수 들어옴");
        Page<RestaurantResponseDTO> page = restaurantService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/degree")
    public ResponseEntity<Page<RestaurantResponseDTO>> getRestaurantsByDegree(@RequestParam(name = "word", required = false) String word,
                                                                              @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/reviews")
    public ResponseEntity<Page<RestaurantResponseDTO>> getRestaurantsByTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                    @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchRestaurants(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurant(@RequestParam(name = "word", required = false) String word,
                                                                          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlycafes")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafe(@RequestParam(name = "word", required = false) String word,
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/routes")
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
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafeForTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                   @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlycafes/degree")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyCafeForDegree(@RequestParam(name = "word", required = false) String word,
                                                                             @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyCafes(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants/reviews")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurantForTotalReviews(@RequestParam(name = "word", required = false) String word,
                                                                                         @PageableDefault(sort = "totalReviews", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search/onlyrestaurants/degree")
    public ResponseEntity<Page<RestaurantResponseDTO>> findOnlyRestaurantForDegree(@RequestParam(name = "word", required = false) String word,
                                                                                   @PageableDefault(sort = "degree", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Page<RestaurantResponseDTO> page = restaurantService.searchOnlyRestaurant(word, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/search/totalPath2")
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
    public ResponseEntity<List<PersonalPathDTO>> getWeightInfo(@RequestBody RecommendedRestaurantsDTO request) {
        List<PersonalPathDTO> personalPathList = pathService.getWeight(request.getKeyword(), request.getStartAddress());
        return ResponseEntity.ok(personalPathList);
    }

    @PostMapping("/search/getWalkPath")
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
    public ResponseEntity<String> addressTest() {
        String address = tmapService.getAddressByCoordinates2(128.3592031, 36.0888125);
       return ResponseEntity.ok(address);
    }
}
