package com.ssd.sthub.dto.complaint;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplaintDTO {

    @Data
    @Getter
    public static class Request {
        private Long secondhandId;
        private Long groupBuyingId;
        private int tag1;
        private int tag2;
        private int tag3;
        private int tag4;
        private int tag5;
        private int tag6;
        private int tag7;
        private int tag8;
        private int tag9;
    }
}
