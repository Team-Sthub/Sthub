package com.ssd.sthub.controller;

import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.PostSecondhandDTO;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final SCommentService sCommentService;
    private final AWSS3SService awss3SService;
    private final HttpServletRequest httpServletRequest;

    // 중고거래 게시글 작성 클릭
    @GetMapping("/moveToForm")
    public String showCreateForm() {
        return "thyme/secondhand/create";
    }

    // 중고거래 게시글 생성
    // Param : @SessionAttribute(name = "memberId") Long memberId
    @PostMapping("/create")
    public ModelAndView createSecondhand(@RequestPart("imgUrl") List<MultipartFile> multipartFiles, @ModelAttribute @Validated PostSecondhandDTO request) {
//        Long memberId = (Long) httpServletRequest.getSession().getAttribute("memberId"); // 세션에서 로그인 한 사용자의 memberId 추출
//        log.info("memberId  : " + memberId);
//        log.info("게시글 생성 세션 Id : " + httpServletRequest.getSession().getId());

        List<String> imgUrls = null;
        if (!multipartFiles.get(0).isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        SecondhandDTO.Response secondhand = secondhandService.createSecondhand(1L, imgUrls, request);
        Long secondhandId = secondhand.getSecondhand().getId();
        log.info("secondhandId =" + secondhandId);
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId);
    }

    // 중고거래 게시글 수정 + 거래 최종 방식 선택
    @PatchMapping("/update")
    public ModelAndView updateSecondhand(@RequestHeader Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @RequestPart @Validated SecondhandDTO.PatchRequest request) throws BadRequestException {
        awss3SService.deleteImages(secondhandService.getImageUrls(request.getSecondhandId())); // 기존 이미지 삭제
        List<String> imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        SecondhandDTO.Response secondhand = secondhandService.updateSecondhand(memberId, imgUrls, request);
        ModelAndView modelAndView = new ModelAndView("redirect:thyme/secondhand/detail");
        modelAndView.addObject("secondhand", secondhand);
        return modelAndView;
    }

    // 중고거래 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse<String>> deleteSecondhand(@RequestHeader Long memberId, @RequestParam Long secondhandId) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.deleteSecondhand(memberId, secondhandId)));
    }

    // 중고거래 게시글 상세 조회 + 판매 내역 상세 조회 + 구매 내역 상세 조회
    @GetMapping("/detail")
    public ModelAndView getSecondhand(@RequestParam Long secondhandId) {
        SecondhandDTO.Response secondhand = secondhandService.getSecondhand(secondhandId);
        ModelAndView modelAndView = new ModelAndView("thyme/secondhand/detail");
        modelAndView.addObject("secondhand", secondhand);
        return modelAndView;
    }

    // 중고거래 게시글 전체 조회
    @GetMapping("/list/{category}")
    public ModelAndView getSecondhands(@PathVariable Category category, @RequestParam int pageNum) throws BadRequestException {
        Page<Secondhand> secondhandList = secondhandService.getSecondhands(category, pageNum);
        ModelAndView modelAndView = new ModelAndView("thyme/secondhand/list");
        modelAndView.addObject("secondhandList", secondhandList);
        return modelAndView;
    }

    // 중고거래 게시글 댓글 작성
    @PostMapping("/detail/comment")
    public ResponseEntity<SuccessResponse<SCommentDTO.Response>> createComment(@RequestHeader Long memberId, @RequestBody SCommentDTO.Request request) {
        return ResponseEntity.ok(SuccessResponse.create(sCommentService.createComment(memberId, request)));
    }

    // 중고거래 게시글 댓글 전체 조회
    @GetMapping("/detail/comment")
    public ResponseEntity<SuccessResponse<List<SCommentDTO.Response>>> getComments(@RequestParam Long secondhandId) {
        return ResponseEntity.ok(SuccessResponse.create(sCommentService.getComments(secondhandId)));
    }

    // 판매내역 전체 조회
    @GetMapping("/selling/list")
    public ResponseEntity<SuccessResponse<Page<Secondhand>>> getSellingSecondhands(@RequestHeader Long memberId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.getSellingSecondhands(memberId, pageNum)));
    }
}
