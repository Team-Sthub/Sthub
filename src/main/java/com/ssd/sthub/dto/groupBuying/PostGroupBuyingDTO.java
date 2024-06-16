package com.ssd.sthub.dto.groupBuying;

import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
        @NotEmpty(message = "제목을 넣어주세요.")
        private String title; // 제목
        @NotNull(message = "카테고리를 넣어주세요.")
        private Category category; // 카테고리
        @NotEmpty(message = "상품을 넣어주세요.")
        private String product; // 상품
        @NotNull(message = "가격을 넣어주세요.")
        private Long price; // 가격
        @NotNull(message = "마감기한을 넣어주세요.")
        private LocalDate deadline; // 마감 기한
        @NotEmpty(message = "오픈 채팅 링크를 넣어주세요.")
        private String chatLink; // 오픈 채팅 링크
        @NotEmpty(message = "직거래 장소를 넣어주세요.")
        private String meetingPlace; // 직거래 장소
        @NotEmpty(message = "내용을 넣어주세요.")
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
