package com.ssd.sthub.dto.groupBuying;

import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostGroupBuyingDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Request {
        private String title; // 제목
        private Category category; // 카테고리
        private String product; // 상품
        private Long price; // 가격
        private LocalDate deadline; // 마감 기한
        private String chatLink; // 오픈 채팅 링크
        private String meetingPlace; // 직거래 장소
        private String content; // 내용
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Response {
        private GroupBuying groupBuying;
        private List<GImage> gImages;
    }
}
