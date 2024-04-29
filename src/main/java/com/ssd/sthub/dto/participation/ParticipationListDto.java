package com.ssd.sthub.dto.participation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParticipationListDto {
    private List<ParticipationListDto.ParticipationDto> participationListDto;
    private int totalPages;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ParticipationDto {
        private Long id;
        private String nickname;
        private String phone;
        private String content;
        private int accept;
    }
}
