package com.ssd.sthub.controller;


import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.AWSS3SService;
import com.ssd.sthub.service.GroupBuyingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/groupBuying")
public class GroupBuyingController {

    private final GroupBuyingService groupBuyingService;
    private final AWSS3SService awss3SService;
    private final HttpServletRequest httpServletRequest;

    // 공동구매 전체 조회
    @GetMapping("/list/{category}")
    public ModelAndView getAllGroupBuying(@PathVariable Category category, @RequestParam int pageNum) throws BadRequestException {
        List<GroupBuyingListDTO.ListResponse> groupBuyingList = groupBuyingService.getAllGroupBuying(category, pageNum - 1);
        ModelAndView modelAndView = new ModelAndView("thyme/groupBuying/list");
        modelAndView.addObject("groupBuyingList", groupBuyingList);
        return modelAndView;
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정) 작성자 수락 여부 확인 후 링크 공개 및 미공개 해야함.
    @GetMapping("/detail")
    public ModelAndView getGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) {
        log.info("detail 드러옴");
        GroupBuyingDetailDTO.Response groupBuying = groupBuyingService.getGroupBuying(memberId, groupBuyingId);

        String nickname = groupBuyingService.getNickname(memberId);

        ModelAndView modelAndView;
        if (memberId == groupBuying.getGroupBuying().getMember().getId()) {
            modelAndView = new ModelAndView("thyme/groupBuying/writerDetail");
        } else {
            modelAndView = new ModelAndView("thyme/groupBuying/detail");
        }
        modelAndView.addObject("groupBuying", groupBuying);
        modelAndView.addObject("memberNickname", nickname);
        return modelAndView;
    }

    // 공동구매 게시글 작성 클릭
    @GetMapping("/moveToForm")
    public String showCreateForm() {
        return "thyme/groupBuying/create";
    }

    // 공동구매 게시글 작성
    @PostMapping("/create")
    public ModelAndView postGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @ModelAttribute PostGroupBuyingDTO.Request request) {
        log.info("memberId  : " + memberId);
        log.info("게시글 생성 세션 Id : " + httpServletRequest.getSession().getId());

        List<String> imgUrls = null;
        if (!multipartFiles.get(0).isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        PostGroupBuyingDTO.Response groupBuying = groupBuyingService.postGroupBuying(memberId, imgUrls, request);
        Long groupBuyingId = groupBuying.getGroupBuying().getId();
        log.info("groupBuyingId =" + groupBuyingId);
        return new ModelAndView("redirect:/groupBuying/detail?groupBuyingId=" + groupBuyingId);
    }

    // 중고거래 게시글 수정 클릭
    @GetMapping("/moveToUpdateForm")
    public ModelAndView showUpdateForm(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) throws BadRequestException {
        GroupBuyingDetailDTO.Response groupBuying = groupBuyingService.getGroupBuying(memberId, groupBuyingId);
        if(!groupBuying.getGroupBuying().getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 수정할 수 있습니다.");
        return new ModelAndView("thyme/groupBuying/update", "groupBuying", groupBuying);
    }

    // 공동구매 게시글 수정
//    @ResponseStatus(HttpStatus.SEE_OTHER) // 리다이렉션 할 때 사용
    @PostMapping("/update")
    public ModelAndView updateGroupBuying(@SessionAttribute(name = "memberId") Long memberId,
                                          @RequestParam(name = "groupBuyingId") Long groupBuyingId,
                                          @RequestPart(name = "imgUrl", required = false) List<MultipartFile> multipartFiles,
                                          @RequestPart GroupBuyingDetailDTO.PatchRequest request) throws BadRequestException {
        log.info("groupBuyingId: " + groupBuyingId);
        log.info("request.getId: " + request.getGroupBuyingId());
        if(groupBuyingId != request.getGroupBuyingId())
            throw new BadRequestException("작성자만 수정할 수 있습니다.");

        List<String> deletedImages = request.getDeleteImages();

        // 삭제된 이미지 경로 처리 (기존 이미지 삭제)
        if (!deletedImages.isEmpty()) {
            awss3SService.deleteImages(deletedImages);
            groupBuyingService.deleteImages(deletedImages);
        }

        List<String> imgUrls = null;
        if(multipartFiles != null && !multipartFiles.isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        GroupBuyingDetailDTO.PatchResponse groupBuying = groupBuyingService.updateGroupBuying(memberId, imgUrls, request);

        log.info("돌아옴 ");
        ModelAndView modelAndView = new ModelAndView("redirect:/groupBuying/detail?groupBuyingId=" + groupBuying.getGroupBuying().getId());
        //modelAndView.addObject("groupBuying", groupBuying); -> 안넣어도 됨
        return modelAndView;
    }

    // 공동구매 게시글 삭제
    @ResponseStatus(HttpStatus.SEE_OTHER) // 리다이렉션 할 때 사용
    @DeleteMapping("/delete")
    public ModelAndView updateGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) {
        String result = groupBuyingService.deleteGroupBuying(memberId, groupBuyingId);
        if(result.equals("delete success"))
            return new ModelAndView("redirect:/groupBuying/list/ALL?pageNum=1");
        else
            return new ModelAndView("redirect:/groupBuying/detail?groupBuying=" + groupBuyingId, "result", "delete fail");
    }

    // 마이페이지 - 공구 내역 (더보기)
    @GetMapping("/mylist/all")
    public ResponseEntity<SuccessResponse<List<GroupBuyingListDTO.MyAllListResponse>>> getAllGroupBuyingByMemberId(@SessionAttribute(name = "memberId") Long memberId, @RequestParam int pageNuM) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.getAllGroupBuyingByMemberId(memberId, pageNuM)));
    }

    // 마이페이지 - 공구 내역 (4개)
    @GetMapping("/mylist")
    public ResponseEntity<SuccessResponse<List<GroupBuyingListDTO.MyListResponse>>> getGroupBuyingsByMemberId(@SessionAttribute(name = "memberId") Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.getGroupBuyingsByMemberId(memberId)));
    }
}
