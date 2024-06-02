package com.ssd.sthub.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class LoginDTO {
    @NotBlank(message = "아이디를 입력하세요.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
