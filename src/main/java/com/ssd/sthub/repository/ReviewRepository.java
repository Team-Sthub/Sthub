package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 거래 후기 내역 조회(키워드 조회)
    List<Integer> findReviewRepoDTOByMemberId(Long memberId);

    // 거래 후기 중복 방지(중고거래 작성ID)
    boolean existsBySecondhandId(Long secondhandId);
}