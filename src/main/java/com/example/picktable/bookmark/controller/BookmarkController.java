package com.example.picktable.bookmark.controller;

import com.example.picktable.bookmark.domain.dto.BookmarkRequestDTO;
import com.example.picktable.bookmark.domain.dto.BookmarkResponseDTO;
import com.example.picktable.common.domain.dto.MsgResponseDTO;
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

    //맛집 즐겨찾기
    @PostMapping("/api/restaurant/{restaurantId}/bookmark")
    public Long bookmark(@PathVariable Long restaurantId) {
        BookmarkRequestDTO bookmarkRequestDTO = new BookmarkRequestDTO();
        bookmarkService.save(restaurantId, bookmarkRequestDTO);
        return restaurantId;
    }

    //즐겨찾기 취소
    @DeleteMapping("/api/restaurant/{restaurantId}/bookmark/{bookmarkId}")
    public ResponseEntity<MsgResponseDTO> deleteBookmark(@PathVariable Long restaurantId, @PathVariable Long bookmarkId) {
        return new ResponseEntity<>(bookmarkService.delete(restaurantId,bookmarkId), HttpStatus.OK);
    }

    //즐겨찾기 조회
    @GetMapping("/restaurant/bookmark")
    public ResponseEntity<Page<BookmarkResponseDTO>> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Page<BookmarkResponseDTO> page = bookmarkService.findAllBookmarks(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
