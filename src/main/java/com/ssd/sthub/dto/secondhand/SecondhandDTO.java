package com.ssd.sthub.dto.secondhand;

import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.domain.enumerate.Transaction;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondhandDTO {
    @Getter
    public static class Request {
        @NotEmpty
        private String title; // 제목
        @NotEmpty
        private Category category; // 카테고리
        @NotEmpty
        private String product; // 상품명
        @NotEmpty
        private Long price; // 가격
        @NotEmpty
        private Transaction type; // 거래 방식
        private String place; // 장소
        @NotEmpty
        private String content; // 내용
        private String imageUrl; // 최대 3개
    }
}
