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
@Table(name = "complaint")
public class Complaint {

    // 신고 식별값
    @Id
    @Column(name="complaintId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    private Boolean tag7;

    @Column(nullable = false)
    private Boolean tag8;

    @Column(nullable = false)
    private Boolean tag9;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 공동구매 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId")
    private GroupBuying secondhand;
}
