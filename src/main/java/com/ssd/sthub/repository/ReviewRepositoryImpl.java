package com.ssd.sthub.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssd.sthub.domain.QReview;
import com.ssd.sthub.domain.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Repository
public class ReviewRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Review.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Integer> findReviewRepoDTOByMemberId(Long memberId) {
        QReview review = QReview.review;

        // 특정 회원의 후기 조회
        List<Review> reviews = jpaQueryFactory
                .selectFrom(review)
                .join(review.secondhand).on(review.secondhand.member.id.eq(memberId))
                .where(review.secondhand.member.id.eq(memberId))
                .fetch();
        log.info("======================1");

        // 모든 후기에서 true 인덱스의 종류 수집
        Set<Integer> trueIndexes = new HashSet<>();
        reviews.forEach(reviewEntity -> {
            List<Integer> tags = reviewEntity.getTags();
            log.info("======================2");
            log.info(tags.toString());
            Set<Integer> indexes = IntStream.range(0, tags.size())
                    .filter(i -> Objects.equals(tags.get(i), 1)) // 1인 경우만 필터링
                    .boxed()
                    .collect(Collectors.toSet());
            trueIndexes.addAll(indexes);
        });

        List<Integer> indexList = new ArrayList<>(trueIndexes);
        return indexList;
    }
}