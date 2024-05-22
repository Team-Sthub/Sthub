package com.ssd.sthub.dto.participation;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationRequestDto {

    @Data
    @Getter
    public static class request {
        private String nickname;
        private String phone;
        private String content;
    }

    @Getter
    public static class PatchRequest {
        private Long participationId;
        private String content;
    }

    @Getter
    public static class AcceptRequest {
        @NotNull
        private Long groupBuyingId;
        private int accept; // 수락/거절 여부
    }
}
