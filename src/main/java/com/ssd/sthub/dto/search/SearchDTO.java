package com.ssd.sthub.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchDTO {
    private Long id; // secondhandId / groupBuyingId 둘 중 하나
    private LocalDateTime createdAt;
    private String imgUrl;
    private String title;
    private String nickname;
    private Long price;
}
