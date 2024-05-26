package com.ssd.sthub.domain;

import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    @ColumnDefault("'모집중'")
    private String status; // 상태 : 모집중, 모집완료

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "groupBuying", cascade = CascadeType.ALL)
    private List<GImage> imageList = new ArrayList<>();

    public GroupBuying(GroupBuyingDetailDTO.PatchRequest groupBuyingDetailDTO, Member member) {
        this.title = groupBuyingDetailDTO.getTitle();
        this.category = groupBuyingDetailDTO.getCategory();
        this.product = groupBuyingDetailDTO.getProduct();
        this.deadline = groupBuyingDetailDTO.getDeadline();
        this.chatLink = groupBuyingDetailDTO.getChatLink();
        this.meetingPlace = groupBuyingDetailDTO.getMeetingPlace();
        this.content = groupBuyingDetailDTO.getContent();
        this.status = groupBuyingDetailDTO.getStatus();
        this.member = member;
    }

    public GroupBuying(PostGroupBuyingDTO postGroupBuyingDTO, Member member) {
        this.title = postGroupBuyingDTO.getTitle();
        this.category = postGroupBuyingDTO.getCategory();
        this.product = postGroupBuyingDTO.getProduct();
        this.price = postGroupBuyingDTO.getPrice();
        this.deadline = postGroupBuyingDTO.getDeadline();
        this.chatLink = postGroupBuyingDTO.getChatLink();
        this.meetingPlace = postGroupBuyingDTO.getMeetingPlace();
        this.content = postGroupBuyingDTO.getContent();
        this.status = postGroupBuyingDTO.getStatus();
        this.member = member;
    }

    public void updateGroupBuying(GroupBuyingDetailDTO.PatchRequest groupBuyingDetailDTO) {
        this.title = groupBuyingDetailDTO.getTitle();
        this.category = groupBuyingDetailDTO.getCategory();
        this.product = groupBuyingDetailDTO.getProduct();
        this.price = groupBuyingDetailDTO.getPrice();
        this.deadline = groupBuyingDetailDTO.getDeadline();
        this.chatLink = groupBuyingDetailDTO.getChatLink();
        this.meetingPlace = groupBuyingDetailDTO.getMeetingPlace();
        this.content = groupBuyingDetailDTO.getContent();
        this.status = groupBuyingDetailDTO.getStatus();
    }

}
