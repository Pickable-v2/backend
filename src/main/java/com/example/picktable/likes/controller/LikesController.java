package com.example.picktable.likes.controller;

import com.example.picktable.likes.service.LikesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Likes", description = "Likes API")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/review/{reviewId}/likes")
    @Operation(description = "리뷰 좋아요 등록")
    public ResponseEntity<?> addReviewLikes(@PathVariable("reviewId") Long reviewId) {
        try {
            likesService.toggleLike(reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to toggle review likes");
        }
    }
}
