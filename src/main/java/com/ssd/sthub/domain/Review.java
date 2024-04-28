package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review {

    // 거래후기 식별값
    @Id
    @Column(name="reviewId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // 별점
    @Column(nullable = false)
    private Double rating;

    // 조건 (각 항목은 노션에 정리되어 있음)
    @Column(nullable = false)
    private Boolean tag1;

    @Column(nullable = false)
    private Boolean tag2;

    @Column(nullable = false)
    private Boolean tag3;

    @Column(nullable = false)
    private Boolean tag4;

    @Column(nullable = false)
    private Boolean tag5;

    @Column(nullable = false)
    private Boolean tag6;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 공동구매 조인

    // 중고거래 조인
}
