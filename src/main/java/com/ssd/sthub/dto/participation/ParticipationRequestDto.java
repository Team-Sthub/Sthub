package com.ssd.sthub.dto.participation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationRequestDto {

    @Data
    @Getter
    public static class Request {
        private String nickname;
        private String phone;
        @NotEmpty(message = "추가사항을 적어주세요. 없으면 '없음'으로 작성해주세요.")
        private String content;
    }

    @Data
    @Getter
    public static class PatchRequest {
        @NotNull
        private Long participationId;
        @NotEmpty
        private String content;
    }

    @Getter
    public static class AcceptRequest {
        @NotNull
        private Long groupBuyingId;
        @NotNull
        private Long participationId;
        private int accept; // 수락/거절 여부
    }
}
