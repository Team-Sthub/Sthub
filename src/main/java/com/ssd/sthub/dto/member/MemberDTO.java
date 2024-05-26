package com.ssd.sthub.dto.member;

import com.ssd.sthub.domain.enumerate.Bank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDTO {

    private String nickname;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$", message = "비밀번호는 영어/숫자/특수문자를 포함해 최소 7자 이상 입력해야 합니다.")
    private String password;
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식은 000-0000-0000 입니다.")
    private String phone;
    private Bank bank;
    private String account;
    private String address;
    private Double latitude;
    private Double longitude;
    @Email(message = "유효한 이메일 주소를 입력하세요.")
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
