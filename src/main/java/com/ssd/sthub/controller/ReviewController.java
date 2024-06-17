package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.dto.review.ReviewRequestDTO;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    // 리뷰 작성
    @PostMapping(value = "")
    public RedirectView createReview(@ModelAttribute @Validated ReviewRequestDTO.Request request, RedirectAttributes redirectAttributes) {
        Long purchaseId = request.getPurchaseId();
        Review review = reviewService.createReview(purchaseId, request);
        redirectAttributes.addFlashAttribute("review", review);
        return new RedirectView("/purchase/list?pageNum=1"); // 중고거래 구매 내역 리스트 페이지로 이동
    }

    // 리뷰 조회
    @GetMapping("/mypage")
    public String getMyReview(@RequestParam(required = false) Long memberId,
                              @SessionAttribute(name = "memberId", required = false) Long sessionMemberId,
                              Model model) {
        // URL 파라미터로 memberId가 전달되지 않은 경우 세션의 memberId를 사용
        if (memberId == null) {
            memberId = sessionMemberId;
        }

        List<String> tags = reviewService.getTags(memberId);
        model.addAttribute("tags", tags);
        model.addAttribute("sessionMemberId", sessionMemberId); // 세션의 memberId 추가
        model.addAttribute("memberId", memberId); // memberId를 모델에 추가

        return "thyme/user/fragments/reviewFragments";
    }
}