package com.example.picktable.bookmark.controller;

import com.example.picktable.bookmark.domain.dto.BookmarkRequestDTO;
import com.example.picktable.bookmark.domain.dto.BookmarkResponseDTO;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/api/restaurant/{restaurantId}/bookmark")
    public Long addRestaurantBookmark(@PathVariable Long restaurantId) {
        BookmarkRequestDTO bookmarkRequestDTO = new BookmarkRequestDTO();
        bookmarkService.addBookmark(restaurantId, bookmarkRequestDTO);
        return restaurantId;
    }

    @DeleteMapping("/api/restaurant/{restaurantId}/bookmark/{bookmarkId}")
    public ResponseEntity<MsgResponseDTO> cancelRestaurantBookmark(@PathVariable Long restaurantId, @PathVariable Long bookmarkId) {
        return new ResponseEntity<>(bookmarkService.deleteBookmark(restaurantId, bookmarkId), HttpStatus.OK);
    }

    @GetMapping("/restaurant/bookmark")
    public ResponseEntity<Page<BookmarkResponseDTO>> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Page<BookmarkResponseDTO> page = bookmarkService.findAllBookmarks(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
