package com.ssd.sthub.controller;


import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import com.ssd.sthub.service.AWSS3SService;
import com.ssd.sthub.service.GroupBuyingService;
import com.ssd.sthub.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final MemberService memberService;

    // 공동구매 전체 조회
    @GetMapping("/list/{category}")
    public ModelAndView getAllGroupBuying(@PathVariable Category category, @RequestParam(defaultValue = "0") int pageNum) {
        ModelAndView modelAndView = new ModelAndView("thyme/groupBuying/list");
        try{
            List<GroupBuyingListDTO.ListResponse> groupBuyingList = groupBuyingService.getAllGroupBuying(category, pageNum - 1);
            modelAndView.addObject("groupBuyingList", groupBuyingList);
        } catch (BadRequestException e) {
            modelAndView.addObject("noList", e.getMessage());
        }

        return modelAndView;
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정) 작성자 수락 여부 확인 후 링크 공개 및 미공개 해야함.
    @GetMapping("/detail")
    public ModelAndView getGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) {
        log.info("detail 드러옴");
        ModelAndView modelAndView = new ModelAndView();

        try {
            GroupBuyingDetailDTO.Response groupBuying = groupBuyingService.getGroupBuying(memberId, groupBuyingId);

            String nickname = groupBuyingService.getNickname(memberId);
            if (memberId == groupBuying.getGroupBuying().getMember().getId()) {
                modelAndView.setViewName("thyme/groupBuying/writerDetail");
            } else {
                modelAndView.setViewName("thyme/groupBuying/detail");
            }
            modelAndView.addObject("groupBuying", groupBuying);
            modelAndView.addObject("memberNickname", nickname);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName("redirect:/groupBuying/list/ALL?pageNum=1");
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    // 공동구매 게시글 작성 클릭
    @GetMapping("/moveToForm")
    public String showCreateForm() {
        return "thyme/groupBuying/create";
    }

    // 공동구매 게시글 작성
    @PostMapping("/create")
    public ModelAndView postGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestPart("imgUrl") List<MultipartFile> multipartFiles, @ModelAttribute @Validated PostGroupBuyingDTO.Request request, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        if(result.hasErrors()) {
            modelAndView.setViewName("thyme/groupBuying/create");
            return modelAndView;
        }

        List<String> imgUrls = null;
        if (!multipartFiles.get(0).isEmpty()) {
            imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
        }

        try {
            PostGroupBuyingDTO.Response groupBuying = groupBuyingService.postGroupBuying(memberId, imgUrls, request);
            Long groupBuyingId = groupBuying.getGroupBuying().getId();
            log.info("groupBuyingId =" + groupBuyingId);
            modelAndView.setViewName("redirect:/groupBuying/detail?groupBuyingId=" + groupBuyingId);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName("thyme/groupBuying/create");
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    // 중고거래 게시글 수정 클릭
    @GetMapping("/moveToUpdateForm")
    public ModelAndView showUpdateForm(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            GroupBuyingDetailDTO.Response groupBuying = groupBuyingService.getGroupBuying(memberId, groupBuyingId);

            if(!groupBuying.getGroupBuying().getMember().getId().equals(memberId))
                throw new BadRequestException("작성자만 수정할 수 있습니다.");

            modelAndView.setViewName("thyme/groupBuying/update");
            modelAndView.addObject("groupBuying", groupBuying);
        } catch (EntityNotFoundException | BadRequestException e) {
            modelAndView.setViewName("redirect:/groupBuying/detail?groupBuyingId=" + groupBuyingId);
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    // 공동구매 게시글 수정
//    @ResponseStatus(HttpStatus.SEE_OTHER) // 리다이렉션 할 때 사용
    @PostMapping("/update")
    public ModelAndView updateGroupBuying(@SessionAttribute(name = "memberId") Long memberId,
                                          @RequestParam(name = "groupBuyingId") Long groupBuyingId,
                                          @RequestPart(name = "imgUrl", required = false) List<MultipartFile> multipartFiles,
                                          @RequestPart GroupBuyingDetailDTO.PatchRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            if (groupBuyingId != request.getGroupBuyingId())
                throw new BadRequestException("작성자만 수정할 수 있습니다.");

            List<String> deletedImages = request.getDeleteImages();

            log.info("====================");
            // 삭제된 이미지 경로 처리 (기존 이미지 삭제)
            if (!deletedImages.isEmpty()) {
                awss3SService.deleteImages(deletedImages);
                groupBuyingService.deleteImages(deletedImages);
            }

            List<String> imgUrls = null;
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                imgUrls = awss3SService.uploadFiles(multipartFiles); // s3 이미지 등록
            }

            GroupBuyingDetailDTO.PatchResponse groupBuying = groupBuyingService.updateGroupBuying(memberId, imgUrls, request);

            modelAndView.setViewName("redirect:/groupBuying/detail?groupBuyingId=" + groupBuying.getGroupBuying().getId());
            //modelAndView.addObject("groupBuying", groupBuying); -> 안넣어도 됨
        } catch (BadRequestException | EntityNotFoundException e) {
            modelAndView.setViewName("redirect:/groupBuying/moveToUpdateForm?groupBuyingId=" + groupBuyingId);
            modelAndView.addObject("errorMessage", e.getMessage());
        } // catch (BadRequestException e) {
//            modelAndView.setViewName("redirect:/groupBuying/detail?groupBuyingId=" + groupBuyingId);
//            modelAndView.addObject("errorMessage", e.getMessage());
//        }
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
    public ModelAndView getAllGroupBuyingByMemberId(@RequestParam(required = false) Long memberId,
                                                    @SessionAttribute(name = "memberId", required = false) Long sessionMemberId,
                                                    @RequestParam int pageNum) {
        // URL 파라미터로 memberId가 전달되지 않은 경우 세션의 memberId를 사용
        if (memberId == null) {
            memberId = sessionMemberId;
        }
        List<GroupBuyingListDTO.MyAllListResponse> myGroupBuyingList = groupBuyingService.getAllGroupBuyingByMemberId(memberId, pageNum - 1);
        return new ModelAndView("thyme/groupBuying/myGroupBuying", "myGroupBuyingList", myGroupBuyingList);
    }

    // 마이페이지 - 공구 내역 (4개)
    @GetMapping("/mylist")
    public String getGroupBuyingsByMemberId(@RequestParam(required = false) Long memberId,
                                            @SessionAttribute(name = "memberId", required = false) Long sessionMemberId,
                                            Model model) {
        // URL 파라미터로 memberId가 전달되지 않은 경우 세션의 memberId를 사용
        if (memberId == null) {
            memberId = sessionMemberId;
        }
        List<GroupBuyingListDTO.MyListResponse> groupBuyingList = groupBuyingService.getGroupBuyingsByMemberId(memberId);
        model.addAttribute("groupBuyingList", groupBuyingList);
        model.addAttribute("sessionMemberId", sessionMemberId); // 세션의 memberId 추가
        return "thyme/user/fragments/groupbuyingFragments";
    }

    // 내 근처 공동구매 조회
    @GetMapping("/around/{category}")
    public ModelAndView getAllAroundGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @PathVariable Category category, @RequestParam int pageNum) {
        ModelAndView modelAndView = new ModelAndView("thyme/groupBuying/around");
        try {
            List<GroupBuyingListDTO.ListResponse> groupBuyingList = groupBuyingService.getAllAroundGroupBuying(memberId, category, pageNum - 1);
            modelAndView.addObject("groupBuyingList", groupBuyingList);
        } catch (EntityNotFoundException | BadRequestException | IllegalArgumentException e) {
            modelAndView.addObject("noList", e.getMessage());
        }

        return modelAndView;
    }
}
