package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.dto.review.ReviewDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;

    // 리뷰 작성폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm(Model model, @SessionAttribute(name = "memberId") Long memberId) {
        log.info("memberId:"+memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        model.addAttribute("member", member);
        return "thyme/review/create";
    }

    // 리뷰 전송
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<Review>> review(@PathVariable("secondhandId") Long secondhandId, @RequestBody ReviewDTO request) {
        return ResponseEntity.ok(SuccessResponse.create(reviewService.createReview(secondhandId, request)));
    }

    // 리뷰 조회

    // 농도 계산
}
