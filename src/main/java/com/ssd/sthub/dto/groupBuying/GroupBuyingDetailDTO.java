package com.ssd.sthub.dto.groupBuying;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.enumerate.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupBuyingDetailDTO {

    private Long id;
    private String title; // 제목
    private Category category; // 카테고리
    private String product; // 상품
    private Long price; // 가격
    private LocalDate deadline; // 마감 기한
    private String chatLink; // 오픈 채팅 링크
    private String meetingPlace; // 직거래 장소
    private String content; // 내용
    private String imageUrl; // 이미지 최대 3개
    private String status; // 상태 : 모집중, 모집완료
}
