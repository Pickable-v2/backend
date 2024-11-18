package com.example.picktable.receipt.controller;

import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.review.domain.entity.Review;
import com.example.picktable.review.domain.type.ReviewType;
import com.example.picktable.receipt.service.ReceiptService;
import com.example.picktable.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiptController {

    @Value("${naver.service.secretKey}")
    private String secretKey;
    private final ReceiptService receiptService;
    private final RestaurantService restaurantService;


    @PostMapping("/api/review/receipt")
    public ResponseEntity<MsgResponseDTO> uploadAndOcr(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("파일이 비어있습니다");
        }

        File tempFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile);

        List<String> ocrResult = receiptService.callAPI("POST", tempFile.getPath(), secretKey, "jpeg");
        tempFile.delete();

        String storeName = extractStoreName(ocrResult);

        if (storeName != null && restaurantService.existsByName(storeName)) {
            Review.builder()
                    .reviewType(ReviewType.CERTIFY)
                    .build();
            return ResponseEntity.ok(new MsgResponseDTO("영수증 인증 완료", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.ok(new MsgResponseDTO("영수증 인증 실패: 상점명을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    private String extractStoreName(List<String> ocrResult) {
        for (int i = 0; i < ocrResult.size(); i++) {
            String line = ocrResult.get(i);
            if (line.contains("매장명") || line.contains("상호명") || line.contains("점포명")) {
                if (i + 1 < ocrResult.size()) {
                    return ocrResult.get(i + 1).trim();
                }
            }
        }
        return null;
    }
}
