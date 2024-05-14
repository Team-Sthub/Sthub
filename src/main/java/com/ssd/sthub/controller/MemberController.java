package com.ssd.sthub.controller;


import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
import com.ssd.sthub.dto.member.UserViewDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    // 아이디 중복 확인
    @GetMapping("/register/check")
    public ResponseEntity<SuccessResponse<String>> checkNickname(@RequestBody String nickname) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.checkNickname(nickname)));
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Member>> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.register(registerDTO)));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Optional<Member>>> login(@RequestBody String nickname, String password) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.login(nickname, password)));
    }

    // 프로필 조회
    @GetMapping("/myPage")
    public ResponseEntity<SuccessResponse<UserViewDTO>> getMember(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.getMember(memberId)));
    }

    // 프로필 상세 조회
    @GetMapping("/myPage/detail")
    public ResponseEntity<SuccessResponse<MemberDTO.MemberResDTO>> getMemberDetail(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.getMemberDetail(memberId)));
    }

    // 마이페이지 정보 수정
    @PatchMapping("/myPage")
    public ResponseEntity<SuccessResponse<Member>> updateMember(@RequestHeader Long memberId, @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(SuccessResponse.create(memberService.updateMember(memberId, memberDTO)));
    }

}
