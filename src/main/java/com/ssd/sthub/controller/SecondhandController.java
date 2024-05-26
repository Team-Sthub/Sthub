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
import net.bytebuddy.matcher.StringMatcher;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // 중고거래 게시글 수정 클릭
    @GetMapping("/moveToUpdateForm")
    public String showUpdateForm() {
        return "thyme/secondhand/update";
    }

    // 거래 최종 방식 클릭
    @GetMapping("/check")
    public ModelAndView showCheckForm(@RequestParam Long secondhandId) {
        return new ModelAndView( "redirect:/secondhand/check", "secondhandId", secondhandId);
    }

    // 중고거래 게시글 수정 + 거래 최종 방식 선택
    @PatchMapping("/update")
    public ModelAndView updateSecondhand(@RequestHeader Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @RequestPart @Validated SecondhandDTO.PatchRequest request) throws BadRequestException {
        awss3SService.deleteImages(secondhandService.getImageUrls(request.getSecondhandId())); // 기존 이미지 삭제
        List<String> imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        SecondhandDTO.DetailResponse secondhand = secondhandService.updateSecondhand(memberId, imgUrls, request);
        ModelAndView modelAndView = new ModelAndView("redirect:/secondhand/detail");
        modelAndView.addObject("secondhand", secondhand);
        return modelAndView;
    }

    // 중고거래 게시글 삭제
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @DeleteMapping("/delete")
    public ModelAndView deleteSecondhand(@SessionAttribute Long memberId, @RequestParam Long secondhandId) throws BadRequestException {
        String result = secondhandService.deleteSecondhand(memberId, secondhandId);

        if(result.equals("delete success"))
            return new ModelAndView("redirect:/secondhand/list/ALL?pageNum=1");
        else
            return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId, "result", "delete fail");
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
    public ModelAndView createComment(@SessionAttribute Long memberId, @ModelAttribute SCommentDTO.Request request) {
        log.info("secondhandId " + request.getSecondhandId());
        SCommentDTO.Response comment = sCommentService.createComment(memberId, request);
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + request.getSecondhandId());
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
