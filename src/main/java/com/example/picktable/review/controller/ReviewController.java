package com.example.picktable.review.controller;

import com.example.picktable.restaurant.domain.dto.RestaurantResponseDTO;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.review.domain.dto.ReviewRequestDTO;
import com.example.picktable.review.domain.dto.ReviewResponseDTO;
import com.example.picktable.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/review/{restaurantId}")
    @Operation(description = "리뷰 등록")
    public ResponseEntity<MsgResponseDTO> addReview(@PathVariable("restaurantId") Long restaurantId, @RequestBody ReviewRequestDTO requestDTO) {
        requestDTO.setTotalLikes(0L);
        reviewService.addReview(restaurantId,requestDTO);
        return ResponseEntity.ok(new MsgResponseDTO("리뷰 등록 완료", HttpStatus.OK.value()));
    }

    @GetMapping("/review/findAll")
    @Operation(description = "리뷰 전체 조회")
    public ResponseEntity<Page<RestaurantResponseDTO>> findAll(@RequestParam(name = "address", required = false) String address,
                                                               Pageable pageable){
        Page<RestaurantResponseDTO> responseDTOS = reviewService.findAll(address,pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @PatchMapping("/review/{reviewId}")
    @Operation(description = "리뷰 수정")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable("reviewId") Long id, @RequestBody ReviewRequestDTO requestDTO) {
        return new ResponseEntity<>(reviewService.updateReview(id, requestDTO), HttpStatus.OK);
    }

    @PatchMapping("/reviewDate")
    @Operation(description = "리뷰 등록 날짜 업데이트")
    public ResponseEntity<MsgResponseDTO> updateDate() {
        reviewService.updateDateFormat();
        return ResponseEntity.ok(new MsgResponseDTO("리뷰 등록 완료", HttpStatus.OK.value()));
    }

    @DeleteMapping("/review/{reviewId}")
    @Operation(description = "리뷰 삭제")
    public ResponseEntity<MsgResponseDTO> deleteReview(@PathVariable("reviewId") Long id) {
        return new ResponseEntity<>(reviewService.deleteReview(id), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/review/{id}")
    @Operation(description = "리뷰 상세 조회")
    public ResponseEntity<ReviewResponseDTO> findReviewDetail(@PathVariable("restaurantId") Long restaurantId,@PathVariable("id") Long id){
        ReviewResponseDTO responseDTOS = reviewService.findReviewDetails(restaurantId,id);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/review/findByAddress/{address}")
    @Operation(description = "주소로부터 리뷰 반환")
    public Page<RestaurantResponseDTO> findAllByAddress(@PathVariable("address") String address, Pageable pageable) {
        return reviewService.findAllByAddress(address, pageable);
    }
}
