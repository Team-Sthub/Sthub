package com.ssd.sthub.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserViewDTO {

    private Long id;
    private String nickname;
    private String profile;
    private Double mannerGrade;
    private String address;
}
