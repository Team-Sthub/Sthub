package com.ssd.sthub.dto.secondhand;

import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.domain.enumerate.Transaction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondhandDTO {
    @Getter
    public static class PostRequest {
        @NotEmpty(message = "제목을 넣어주세요.")
        private String title; // 제목
        @NotNull(message = "카테고리를 넣어주세요.")
        private Category category; // 카테고리
        @NotEmpty(message = "상품명을 넣어주세요.")
        private String product; // 상품명
        @NotNull(message = "가격을 넣어주세요.")
        private Long price; // 가격
        @NotNull(message = "거래 방식을 넣어주세요.")
        private Transaction type; // 거래 방식
        private String place; // 장소
        @NotEmpty(message = "내용을 넣어주세요.")
        private String content; // 내용
    }

    @Getter
    public static class PatchRequest {
        @NotNull
        private Long secondhandId;
        @NotEmpty
        private String title; // 제목
        @NotNull
        private Category category; // 카테고리
        @NotEmpty
        private String product; // 상품명
        @NotNull
        private Long price; // 가격
        @NotNull
        private Transaction type; // 거래 방식
        private String place; // 장소
        private String trackingNum; // 운송장 번호
        @NotEmpty
        private String content; // 내용
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Secondhand secondhand;
        private List<SImage> sImages;
    }
}
