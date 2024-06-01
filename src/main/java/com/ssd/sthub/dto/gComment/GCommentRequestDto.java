package com.ssd.sthub.dto.gComment;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GCommentRequestDto {

    @Getter
    @Data
    public static class request {
        private Long groupBuyingId;
        @NotEmpty(message = "댓글을 작성해주세요.")
        private String content;
    }
}
