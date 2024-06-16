package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Review;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.review.ReviewRequestDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.repository.ReviewRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SecondhandRepository secondhandRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;

    // 후기 작성
    public Review createReview(Long purchaseId, ReviewRequestDTO.Request request) throws NullPointerException {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 구매 내역을 찾을 수 없습니다."));

        Secondhand secondhand = secondhandRepository.findById(purchase.getSecondhand().getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));

        // 거래후기 작성 중복 방지
        if (reviewRepository.existsBySecondhandId(secondhand.getId())) {
            throw new IllegalStateException("이미 리뷰가 작성된 구매 내역입니다.");
        }

        Review review = new Review(request, secondhand);

        //매너 농도 계산
        Member member = secondhand.getMember();
        member.updateMannerGrade(request);
        memberRepository.save(member);

        return reviewRepository.save(review);
    }

    // 후기 조회 - 중복 제거
    public List<String> getTags(Long memberId) {
        List<Review> reviews = reviewRepository.findReviewBySecondhand_MemberId(memberId);
        Set<String> uniqueTags = new HashSet<>();

        for (Review review : reviews) {
            List<Integer> reviewTags = review.getTags();
            for (int i = 0; i < reviewTags.size(); i++) {
                if (reviewTags.get(i) == 1) {
                    uniqueTags.add(convertIndexToTag(i));
                }
            }
        }

        return new ArrayList<>(uniqueTags);
    }

    private String convertIndexToTag(int index) {
        switch (index) {
            case 0:
                return "# 연락이 빨라요 📞";
            case 1:
                return "# 친절하고 매너가 좋아요 😄";
            case 2:
                return "# 좋은 물건을 저렴하게 판매해요☺️";
            case 3:
                return "# 시간 약속을 잘 지켜요 🫰🏻";
            case 4:
                return "# 물건 설명이 자세해요 📝";
            case 5:
                return "# 물건 상태가 설명과 같아요 🔧";
            // 필요한 경우 더 많은 태그를 추가
            default:
                return "# 알 수 없는 태그";
        }
    }

    // 매너 농도 조회

}