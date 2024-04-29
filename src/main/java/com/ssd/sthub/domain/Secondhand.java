package com.ssd.sthub.domain;

import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.domain.enumerate.Transaction;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "secondhand")
public class Secondhand extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "secondhandId")
    private Long id;

    @Column(nullable = false)
    private String title; // 제목

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 카테고리

    @Column(nullable = false)
    private String product; // 상품명

    @Column(nullable = false)
    private Long price; // 가격

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transaction type; // 거래 방식

    private String place; // 장소

    @Column(nullable = false)
    private String content; //내용

    private String trackingNum; // 운송장 번호

    @ColumnDefault("'거래가능'")
    @Column(nullable = false)
    private String status; // 거래가능, 예약중, 거래완료

    @Lob
    private String imageUrl; // 최대 3개

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Secondhand(Member member, SecondhandDTO.PostRequest request) {
        this.member = member;
        this.title = request.getTitle();
        this.category = request.getCategory();
        this.product = request.getProduct();
        this.price = request.getPrice();
        this.type = request.getType();
        this.place = request.getPlace();
        this.content = request.getContent();
        this.imageUrl = request.getImageUrl() != null ? request.getImageUrl() : "";
    }

    public void update(SecondhandDTO.PatchRequest request) {
        this.title = request.getTitle();
        this.category = request.getCategory();
        this.product = request.getProduct();
        this.price = request.getPrice();
        this.type = request.getType();
        this.place = request.getPlace();
        this.trackingNum = request.getTrackingNum();
        this.content = request.getContent();
        this.imageUrl = request.getImageUrl() != null ? request.getImageUrl() : "";
    }
}
