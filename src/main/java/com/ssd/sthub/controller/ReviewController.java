package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.dto.review.ReviewRequestDTO;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final PurchaseRepository purchaseRepository;

    // 리뷰 작성폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm(Model model, @RequestParam(name = "purchaseId") Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("구매내역 조회에 실패했습니다."));
        model.addAttribute("purchase", purchase);
        model.addAttribute("reviewRequestDto", new ReviewRequestDTO.Request());
        return "thyme/review/create";
    }

    // 리뷰 전송
    @PostMapping(value = "/create")
    public ModelAndView createReview(@ModelAttribute @Validated ReviewRequestDTO.Request request) {
        Long purchaseId = request.getPurchaseId();
        Review review = reviewService.createReview(purchaseId, request);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("review", review);
        modelAndView.setViewName("thyme/user/login"); // 구매 내역으로 다시 가게 해야됨!
        return modelAndView;
    }

    // 리뷰 조회
    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<List<Integer>>> getMyReview(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(reviewService.getTags(memberId)));
    }
    // 농도 계산
}
