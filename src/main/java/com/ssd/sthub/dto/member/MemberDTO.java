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

    private Long id;
    private String nickname;
    private String password;
    private String phone;
    private Bank bank;
    private String account;
    private String address;
    private String email;
    private String profile;
}
