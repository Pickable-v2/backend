package com.example.picktable.review.controller;

import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.common.domain.dto.MsgResponseDTO;
import com.example.picktable.review.domain.dto.ReviewRequestDTO;
import com.example.picktable.review.domain.dto.ReviewResponseDTO;
import com.example.picktable.review.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/api/review/{restaurantId}")
    public ResponseEntity<MsgResponseDTO> addReview(@PathVariable("restaurantId") Long restaurantId, @RequestBody ReviewRequestDTO requestDTO) {
        requestDTO.setTotalLikes(0L);
        reviewService.addReview(restaurantId,requestDTO);
        return ResponseEntity.ok(new MsgResponseDTO("리뷰 등록 완료", HttpStatus.OK.value()));
    }

    @GetMapping("/review/findAll")
    public ResponseEntity<Page<RestaurantResponseDTO>> findAll(@RequestParam(name = "address", required = false) String address,
                                                               Pageable pageable){
        Page<RestaurantResponseDTO> responseDTOS = reviewService.findAll(address,pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PatchMapping("/api/review/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable("reviewId") Long id, @RequestBody ReviewRequestDTO requestDTO) {
        return new ResponseEntity<>(reviewService.updateReview(id, requestDTO), HttpStatus.OK);
    }

    @PatchMapping("/api/reviewDate/")
    public ResponseEntity<MsgResponseDTO> updateDate() {
        reviewService.updateDateFormat();
        return ResponseEntity.ok(new MsgResponseDTO("리뷰 등록 완료", HttpStatus.OK.value()));
    }

    @DeleteMapping("/api/review/{reviewId}")
    public ResponseEntity<MsgResponseDTO> deleteReview(@PathVariable("reviewId") Long id) {
        return new ResponseEntity<>(reviewService.deleteReview(id), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/review/{id}")
    public ResponseEntity<ReviewResponseDTO> findReviewDetail(@PathVariable("restaurantId") Long restaurantId,@PathVariable("id") Long id){
        ReviewResponseDTO responseDTOS = reviewService.findReviewDetails(restaurantId,id);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/review/findbyAddress/{address}")
    public Page<RestaurantResponseDTO> findAllByAddress(@PathVariable("address") String address, Pageable pageable) {
        return reviewService.findAllByAddress(address, pageable);
    }
}
