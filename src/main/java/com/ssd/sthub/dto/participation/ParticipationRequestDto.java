package com.ssd.sthub.dto.participation;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationRequestDto {

    @Getter
    public static class request {
        private String nickname;
        private String phone;
        private String content;
    }

    @Getter
    public static class PatchRequest {
        @NotNull
        private Long groupBuyingId;
        private int accept; // 수락/거절 여부
    }
}
