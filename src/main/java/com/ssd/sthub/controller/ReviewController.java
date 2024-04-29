package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.dto.review.ReviewDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ReviewQueryService;
import com.ssd.sthub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewQueryService reviewQueryService;

    // 리뷰 전송
    @PostMapping("/{purchaseId}/create")
    public ResponseEntity<SuccessResponse<Review>> review(@PathVariable("purchaseId") Long purchaseId, @RequestBody ReviewDTO request) {
        return ResponseEntity.ok(SuccessResponse.create(reviewService.createReview(purchaseId, request)));
    }

    // 리뷰 조회

    // 농도 계산
}
