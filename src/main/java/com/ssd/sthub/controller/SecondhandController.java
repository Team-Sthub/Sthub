package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final SCommentService sCommentService;

    // 중고거래 게시글 작성 클릭
    @GetMapping("/moveToForm")
    public String showCreateForm() {
        return "thyme/secondhand/create";
    }

    // 중고거래 게시글 생성
    @PostMapping("/create")
    public ModelAndView createSecondhand(@RequestHeader Long memberId, @RequestBody SecondhandDTO.PostRequest request) {
        Secondhand secondhand = secondhandService.createSecondhand(memberId, request);
        ModelAndView modelAndView = new ModelAndView("redirect:thyme/secondhand/detail");
        modelAndView.addObject("secondhand", secondhand);
        return modelAndView;
    }

    // 중고거래 게시글 수정 + 거래 최종 방식 선택
    @PatchMapping("/update")
    public ResponseEntity<SuccessResponse<Secondhand>> updateSecondhand(@RequestHeader Long memberId, @RequestBody SecondhandDTO.PatchRequest request) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.updateSecondhand(memberId, request)));
    }

    // 중고거래 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse<String>> deleteSecondhand(@RequestHeader Long memberId, @RequestParam Long secondhandId) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.deleteSecondhand(memberId, secondhandId)));
    }

    // 중고거래 게시글 상세 조회 + 판매 내역 상세 조회 + 구매 내역 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<Secondhand>> getSecondhand(@RequestParam Long secondhandId) {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.getSecondhand(secondhandId)));
    }

    // 중고거래 게시글 전체 조회
    @GetMapping("/list/{category}")
    public ModelAndView getSecondhands(@PathVariable Category category, @RequestParam int pageNum) throws BadRequestException {
        Page<Secondhand> secondhandList = secondhandService.getSecondhands(category, pageNum);
        ModelAndView modelAndView = new ModelAndView("redirect:thyme/secondhand/list");
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
