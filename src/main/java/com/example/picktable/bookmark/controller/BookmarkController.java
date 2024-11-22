package com.example.picktable.bookmark.controller;

import com.example.picktable.bookmark.domain.dto.BookmarkRequestDTO;
import com.example.picktable.bookmark.domain.dto.BookmarkResponseDTO;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.bookmark.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bookmark", description = "Bookmark API")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/restaurant/{restaurantId}/bookmark")
    @Operation(description = "음식점 즐겨찾기 등록")
    public Long addRestaurantBookmark(@PathVariable Long restaurantId) {
        BookmarkRequestDTO bookmarkRequestDTO = new BookmarkRequestDTO();
        bookmarkService.addBookmark(restaurantId, bookmarkRequestDTO);
        return restaurantId;
    }

    @DeleteMapping("/restaurant/bookmark/{bookmarkId}")
    @Operation(description = "음식점 즐겨찾기 해제")
    public ResponseEntity<MsgResponseDTO> cancelRestaurantBookmark( @PathVariable Long bookmarkId) {
        return new ResponseEntity<>(bookmarkService.cancelBookmark(bookmarkId), HttpStatus.OK);
    }

    @GetMapping("/restaurant/bookmark")
    @Operation(description = "음식점 즐겨찾기 전체 조회")
    public ResponseEntity<Page<BookmarkResponseDTO>> findAllBookmarks(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookmarkResponseDTO> page = bookmarkService.findAllBookmarks(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
