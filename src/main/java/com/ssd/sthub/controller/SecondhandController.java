package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final SCommentService sCommentService;
    private final AWSS3SService awss3SService;

    // 중고거래 게시글 작성 클릭
    @GetMapping("/moveToForm")
    public String showCreateForm() {
        return "thyme/secondhand/create";
    }

    // 중고거래 게시글 생성
    @PostMapping("/create")
    public ModelAndView createSecondhand(@SessionAttribute Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @ModelAttribute @Validated SecondhandDTO.PostRequest request) {
        List<String> imgUrls = null;
        if (!multipartFiles.get(0).isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        SecondhandDTO.DetailResponse secondhand = secondhandService.createSecondhand(memberId, imgUrls, request);
        Long secondhandId = secondhand.getSecondhand().getId();
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId);
    }

    // 중고거래 게시글 수정 + 거래 최종 방식 선택
    @PatchMapping("/update")
    public ModelAndView updateSecondhand(@RequestHeader Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @RequestPart @Validated SecondhandDTO.PatchRequest request) throws BadRequestException {
        awss3SService.deleteImages(secondhandService.getImageUrls(request.getSecondhandId())); // 기존 이미지 삭제
        List<String> imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        SecondhandDTO.DetailResponse secondhand = secondhandService.updateSecondhand(memberId, imgUrls, request);
        ModelAndView modelAndView = new ModelAndView("redirect:thyme/secondhand/detail");
        modelAndView.addObject("secondhand", secondhand);
        return modelAndView;
    }

    // 중고거래 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse<String>> deleteSecondhand(@SessionAttribute Long memberId, @RequestParam Long secondhandId) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.deleteSecondhand(memberId, secondhandId)));
    }

    // 중고거래 게시글 상세 조회 + 판매 내역 상세 조회 + 구매 내역 상세 조회
    @GetMapping("/detail")
    public ModelAndView getSecondhand(@SessionAttribute Long memberId, @RequestParam Long secondhandId) {
        SecondhandDTO.DetailResponse secondhand = secondhandService.getSecondhand(secondhandId);
        Long writerId = secondhand.getSecondhand().getMember().getId();

        // 작성자일 때와 작성자가 아닐 때의 View가 다름
        if(Objects.equals(memberId, writerId))
            return new ModelAndView("thyme/secondhand/writerDetail", "secondhand", secondhand);
        else
            return new ModelAndView("thyme/secondhand/detail", "secondhand", secondhand);
    }

    // 중고거래 게시글 전체 조회
    @GetMapping("/list/{category}")
    public ModelAndView getSecondhands(@PathVariable Category category, @RequestParam int pageNum) throws BadRequestException {
        List<SecondhandDTO.ListResponse> secondhandList = secondhandService.getSecondhands(category, pageNum - 1);
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

    @GetMapping("/test")
    public String test() {
        return "thyme/secondhand/list";
    }
}
