package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.member.LoginDTO;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
import com.ssd.sthub.dto.member.UserViewDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.AWSS3SService;
import com.ssd.sthub.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {
    private final AWSS3SService awss3SService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 로그인 아이콘 클릭
    @GetMapping("/login")
    public String showLoginForm() { return "thyme/user/login";  }

    // 회원가입 버튼 클릭
    @GetMapping("/register")
    public String showRegisterForm() { return "thyme/user/register";  }

    // 아이디 중복 확인
    @GetMapping("/register/check")
    public ResponseEntity<SuccessResponse<String>> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = memberService.checkNickname(nickname);    // 존재하면 true
        if (isAvailable) {
            return ResponseEntity.ok(SuccessResponse.create("사용 가능한 아이디입니다."));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(SuccessResponse.create("이미 사용 중인 아이디입니다."));
        }
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestPart(value = "profile", required = false) MultipartFile multipartFile,
                                      @Valid @ModelAttribute RegisterDTO request,
                                      BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        String imgUrl = null;
        if (multipartFile != null) {
            imgUrl = awss3SService.uploadFile(multipartFile);
        }
        memberService.register(imgUrl, request);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "회원가입에 성공하였습니다.");
        return ResponseEntity.ok(successResponse);
    }

    // 로그인
    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest httpServletRequest, @ModelAttribute LoginDTO loginDTO) {
        ModelAndView mav = new ModelAndView();
        try {
            Member member = memberService.login(loginDTO);

            httpServletRequest.getSession().invalidate(); // 세션 생성 전 기존 세션 파기
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("memberId", member.getId()); // 세션에 로그인 한 사용자의 memberId 등록
            session.setMaxInactiveInterval(1800); // session이 30분동안 유지
            mav.setViewName("redirect:/secondhand/list/ALL?pageNum=1");
        } catch (EntityNotFoundException e) {
            mav.setViewName("thyme/user/login"); // 로그인 페이지로 다시 보냄
            mav.addObject("errorMessage", e.getMessage());
        }
        return mav;
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/thyme/user/login";
    }

    // 프로필 조회
    @GetMapping("/myPage")
    public ModelAndView getMember(@SessionAttribute(name = "memberId") Long memberId) {
        UserViewDTO user = memberService.getMember(memberId);
        ModelAndView mav = new ModelAndView("thyme/user/userView");
        mav.addObject("user", user);
        return mav;
    }

    // 프로필 상세 조회
    @GetMapping("/myPage/detail")
    public ModelAndView getMemberDetail(@SessionAttribute(name = "memberId") Long memberId) {
        MemberDTO.MemberResDTO detailUser = memberService.getMemberDetail(memberId);
        ModelAndView mav = new ModelAndView("thyme/user/userInfo");
        mav.addObject("user", detailUser);
        return mav;
    }

    // 마이페이지 정보 수정
    @PatchMapping("/myPage")
    public ResponseEntity<?> updateMember(@SessionAttribute(name = "memberId") Long memberId,
                                          @RequestPart(value = "profile", required = false) MultipartFile multipartFile,
                                          @Valid @ModelAttribute MemberDTO memberDTO,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        // 변경된 필드만 업데이트
        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
            existingMember.setPassword(memberDTO.getPassword());
        }
        if (memberDTO.getPhone() != null && !memberDTO.getPhone().isEmpty()) {
            existingMember.setPhone(memberDTO.getPhone());
        }
        if (memberDTO.getEmail() != null && !memberDTO.getEmail().isEmpty()) {
            existingMember.setEmail(memberDTO.getEmail());
        }
        if (memberDTO.getBank() != null) {
            existingMember.setBank(memberDTO.getBank());
        }
        if (memberDTO.getAccount() != null && !memberDTO.getAccount().isEmpty()) {
            existingMember.setAccount(memberDTO.getAccount());
        }
        if (memberDTO.getAddress() != null && !memberDTO.getAddress().isEmpty()) {
            existingMember.setAddress(memberDTO.getAddress());
        }
        if (memberDTO.getLatitude() != null) {
            existingMember.setLatitude(memberDTO.getLatitude());
        }
        if (memberDTO.getLongitude() != null) {
            existingMember.setLongitude(memberDTO.getLongitude());
        }

        String imgUrl = existingMember.getProfile(); // 초기값을 기존 이미지 URL로 설정
        if (multipartFile != null && !multipartFile.isEmpty()) {
            imgUrl = awss3SService.uploadFile(multipartFile);
        }
        existingMember.setProfile(imgUrl);

        memberService.updateMember(existingMember);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "정보수정에 성공하였습니다.");
        return ResponseEntity.ok(successResponse);
    }

}
