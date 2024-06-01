package com.ssd.sthub.dto.participation;

import com.ssd.sthub.domain.Participation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParticipationResponseDto {
    private List<ParticipationResponseDto.ParticipationDto> participationListDto;
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

        public ParticipationDto(Participation participation) {
            this.id = participation.getId();
            this.content = participation.getContent();
            this.accept = participation.getAccept();
        }
    }
}




