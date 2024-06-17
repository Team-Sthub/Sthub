package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final MemberService memberService;
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
    public ModelAndView showUpdateForm(@RequestParam Long secondhandId) {
        SecondhandDTO.DetailResponse secondhand = secondhandService.getSecondhand(secondhandId);
        return new ModelAndView("thyme/secondhand/update", "secondhand", secondhand);
    }

    // 거래 최종 방식 클릭
    @GetMapping("/check")
    public ModelAndView showCheckForm(@RequestParam Long secondhandId) {
        return new ModelAndView( "redirect:/secondhand/check", "secondhandId", secondhandId);
    }

    // 중고거래 게시글 수정
    @PostMapping("/update")
    public ModelAndView updateSecondhand(@SessionAttribute Long memberId,
                                         @RequestPart(value="imgUrl", required = false) List<MultipartFile> multipartFiles,
                                         @RequestPart @Validated SecondhandDTO.PatchRequest request) throws BadRequestException {
        List<String> deletedImages = request.getDeleteImages();

        // 삭제된 이미지 경로 처리
        if (!deletedImages.isEmpty()) {
            awss3SService.deleteImages(deletedImages);
            secondhandService.deleteImages(deletedImages);
        }

        List<String> imgUrls = null;
        if(multipartFiles != null && !multipartFiles.isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        SecondhandDTO.DetailResponse secondhand = secondhandService.updateSecondhand(memberId, imgUrls, request);
        Long secondhandId = request.getSecondhandId();
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId);
    }

    // 중고거래 거래 최종 방식 수정
    @PostMapping("/check")
    public ModelAndView checkTransaction(@SessionAttribute Long memberId,
                                         @RequestBody @Validated SecondhandDTO.CheckRequest request) throws BadRequestException {
        SecondhandDTO.DetailResponse secondhand = secondhandService.checkSecondhand(memberId, request);
        Long secondhandId = request.getSecondhandId();
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId);
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

        String nickname = memberService.getMember(memberId).getNickname();
        ModelAndView modelAndView = null;

        // 작성자일 때와 작성자가 아닐 때의 View가 다름
        if(String.valueOf(writerId).equals(String.valueOf(memberId))) {
            modelAndView = new ModelAndView("thyme/secondhand/writerDetail", "secondhand", secondhand);
        }
        else
            modelAndView = new ModelAndView("thyme/secondhand/detail", "secondhand", secondhand);

        modelAndView.addObject("nickname", nickname);
        return modelAndView;
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
    @GetMapping("/selling/list/{status}")
    public ModelAndView getSellingSecondhands(@PathVariable String status,
                                              @RequestParam(required = false) Long memberId,
                                              @SessionAttribute(name = "memberId", required = false) Long sessionMemberId,
                                              @RequestParam int pageNum) throws BadRequestException {
        // URL 파라미터로 memberId가 전달되지 않은 경우 세션의 memberId를 사용
        if (memberId == null) {
            memberId = sessionMemberId;
        }
        List<SecondhandDTO.ListResponse> secondhandList = secondhandService.getSellingSecondhands(status, memberId, pageNum - 1);

        // memberId가 있을 경우 "otherSelling" 뷰로, 없을 경우 "mySelling" 뷰로 이동
        ModelAndView modelAndView;
        if (memberId.equals(sessionMemberId)) {
            modelAndView = new ModelAndView("thyme/secondhand/mySelling");
        } else {
            modelAndView = new ModelAndView("thyme/secondhand/otherSelling");
            if (secondhandList == null || secondhandList.size() == 0) {
                secondhandList = Collections.emptyList();
            }
        }
        modelAndView.addObject("secondhandList", secondhandList);
        modelAndView.addObject("status", status);
        return modelAndView;
    }

    // 판매내역 상위 4개 조회
    @GetMapping("/selling/top4List")
    public String getSellingSecondhands(@RequestParam(required = false) Long memberId,
                                        @SessionAttribute(name = "memberId", required = false) Long sessionMemberId,
                                        Model model) throws BadRequestException {
        // URL 파라미터로 memberId가 전달되지 않은 경우 세션의 memberId를 사용
        if (memberId == null) {
            memberId = sessionMemberId;
        }

        List<SecondhandDTO.Top4ListResponse> secondhandList = secondhandService.getTop4Items(memberId);
        model.addAttribute("secondhandList", secondhandList);
        model.addAttribute("sessionMemberId", sessionMemberId); // 세션의 memberId 추가
        return "thyme/user/fragments/sellingFragments";
    }

    // 배송조회 폼으로 이동
    @GetMapping("tracker")
    public ModelAndView moveToTrackerForm(@RequestParam Long secondhandId) {
        SecondhandDTO.DetailResponse secondhand = secondhandService.getSecondhand(secondhandId);
        String trackingNum = secondhand.getSecondhand().getTrackingNum();

        if(trackingNum != null)
            return new ModelAndView("thyme/secondhand/tracker", "secondhand", secondhand);
        return new ModelAndView("redirect:/purchase/list?pageNum=1", "errorMessage", "운송장번호가 등록되지 않았습니다.");
    }

    // 내 근처 중고거래 조회
    @GetMapping("/around/{category}")
    public ModelAndView getAllAroundSecondhand(@SessionAttribute(name = "memberId") Long memberId, @PathVariable Category category, @RequestParam int pageNum) {
        ModelAndView modelAndView = new ModelAndView("thyme/secondhand/around");
        try {
            List<SecondhandDTO.ListResponse> secondhandList = secondhandService.getAllAroundSecondhand(memberId, category, pageNum - 1);
            modelAndView.addObject("secondhandList", secondhandList);
        } catch (EntityNotFoundException | BadRequestException | IllegalArgumentException e) {
            modelAndView.addObject("noList", e.getMessage());
        }

        return modelAndView;
    }

}
