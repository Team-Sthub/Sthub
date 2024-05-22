package com.ssd.sthub.controller;


import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.LoginDTO;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
import com.ssd.sthub.dto.member.UserViewDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    // 로그인 아이콘 클릭
    @GetMapping("/login")
    public String showLoginForm() { return "thyme/user/login";  }

    // 회원가입 버튼 클릭
    @GetMapping("/register")
    public String showRegisterForm() { return "thyme/user/register";  }

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
    public ModelAndView login(HttpServletRequest httpServletRequest, @ModelAttribute LoginDTO loginDTO) {
        Member member = memberService.login(loginDTO);

        httpServletRequest.getSession().invalidate(); // 세션 생성 전 기존 세션 파기
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("memberId", member.getId()); // 세션에 로그인 한 사용자의 memberId 등록
        session.setMaxInactiveInterval(1800); // session이 30분동안 유지

        return new ModelAndView("redirect:/secondhand/list/ALL?pageNum=1");
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
