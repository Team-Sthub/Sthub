package com.ssd.sthub.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDTO {

    @Data
    @Getter
    public static class Request {
        private Long purchaseId;
        @NotNull(message = "거래 후기 점수를 매겨 주세요.")
        @Min(value = 1, message = "점수는 1점 이상이어야 합니다.")
        @Max(value = 5, message = "점수는 5점 이하이어야 합니다.")
        private Integer rating;
        private int tag1;
        private int tag2;
        private int tag3;
        private int tag4;
        private int tag5;
        private int tag6;
    }

}
