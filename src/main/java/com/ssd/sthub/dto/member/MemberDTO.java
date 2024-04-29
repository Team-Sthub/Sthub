package com.ssd.sthub.dto.member;

import com.ssd.sthub.domain.enumerate.Bank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDTO {

    private String nickname;
    private String password;
    private String phone;
    private Bank bank;
    private String account;
    private String address;
    private Double latitude;
    private Double longitude;
    private String email;
    private String profile;
    private Double mannerGrade;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    static public class MemberResDTO {
        private Long id;
        private String nickname;
        private String password;
        private String phone;
        private Bank bank;
        private String account;
        private String address;
        private Double latitude;
        private Double longitude;
        private String email;
        private String profile;
        private Double mannerGrade;
    }
}
