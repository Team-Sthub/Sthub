package com.ssd.sthub.dto.groupBuying;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupBuyingListDTO {

    private List<GroupBuyingDTO> groupBuyingListDto;
    private int totalPages;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GroupBuyingDTO {
        private Long id;
        private String title;
        private Long price;
        private String nickname;
        private String status;
    }

}
