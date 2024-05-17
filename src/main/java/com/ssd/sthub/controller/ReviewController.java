package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Review;
import com.ssd.sthub.dto.review.ReviewDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 전송
    @PostMapping("/{purchaseId}/create")
    public ResponseEntity<SuccessResponse<Review>> review(@PathVariable("purchaseId") Long purchaseId, @RequestBody ReviewDTO request) {
        return ResponseEntity.ok(SuccessResponse.create(reviewService.createReview(purchaseId, request)));
    }

    // 리뷰 조회
    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<List<Integer>>> getMyReview(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(reviewService.getTags(memberId)));
    }

    // 농도 계산
}
