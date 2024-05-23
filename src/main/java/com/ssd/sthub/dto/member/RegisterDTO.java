package com.ssd.sthub.dto.member;

import com.ssd.sthub.domain.enumerate.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$", message = "비밀번호는 영어/숫자/특수문자를 포함해 최소 7자 이상 입력해야 합니다.")
    private String password;
    @NotBlank(message = "전화번호를 입력하세요.")
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식은 000-0000-0000 입니다.")
    private String phone;
    @NotNull(message = "은행을 선택해주세요.")
    private Bank bank;
    @NotNull(message = "계좌번호를 입력해주세요.")
    private String account;
    private String address;
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    private String profile;
    private Double latitude;
    private Double longitude;
}
