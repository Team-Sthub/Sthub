package com.ssd.sthub.dto.gComment;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GCommentRequestDto {

    @Getter
    @Data
    public static class request {
        private Long groupBuyingId;
        private String content;
    }
}
