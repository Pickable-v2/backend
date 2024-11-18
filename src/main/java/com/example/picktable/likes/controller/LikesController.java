package com.example.picktable.likes.controller;

import com.example.picktable.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/api/review/{reviewId}/likes")
    public ResponseEntity<?> addReviewLikes(@PathVariable("reviewId") Long reviewId) {
        try {
            likesService.toggleLike(reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to toggle review likes");
        }
    }
}
