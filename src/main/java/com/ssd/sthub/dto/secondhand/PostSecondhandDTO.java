package com.ssd.sthub.dto.secondhand;

import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.domain.enumerate.Transaction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSecondhandDTO {
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
