package com.ssd.sthub.dto.participation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParticipationRequestDto {

    @Getter
    public static class request {
        private String nickname;
        private String phone;
        private String content;
    }
}
