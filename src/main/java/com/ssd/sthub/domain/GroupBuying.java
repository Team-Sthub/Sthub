package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "groupBuying")
public class GroupBuying extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupBuyingId")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title; // 제목

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    @Column(nullable = false)
    private String product; // 상품

    @Column(nullable = false)
    private Long price; // 가격

    @Column(nullable = false)
    private LocalDate deadline; // 마감 기한

    @Column(nullable = false)
    private String chatLink; // 오픈 채팅 링크

    @Column
    private String meetingPlace; // 직거래 장소

    @Column(nullable = false)
    private String content; // 내용

    @Lob
    private String imageUrl; // 이미지 최대 3개

    @Column(nullable = false)
    @ColumnDefault("'모집중'")
    private String status; // 상태 : 모집중, 모집완료

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;
}
