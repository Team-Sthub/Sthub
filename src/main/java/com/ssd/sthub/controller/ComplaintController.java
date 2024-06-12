package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ComplaintService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/complaint")
public class ComplaintController {
    private final ComplaintService complaintService;
    private final SecondhandRepository secondhandRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 신고 작성폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm(Model model,
                                 @RequestParam(name = "secondhandId", required = false) Long secondhandId,
                                 @RequestParam(name = "groupBuyingId", required = false) Long groupBuyingId) {
        if (secondhandId != null) {
            Secondhand secondhand = secondhandRepository.findById(secondhandId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다."));
            model.addAttribute("secondhand", secondhand);
        }

        if (groupBuyingId != null) {
            GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다."));
            model.addAttribute("groupBuying", groupBuying);
        }

        model.addAttribute("complaintRequestDto", new ComplaintDTO.Request());
        return "thyme/complaint/create";
    }

    // 중고거래 신고 처리
    @PostMapping("/secondhand/{secondhandId}")
    public ModelAndView complaintSecondhand(@PathVariable("secondhandId") Long secondhandId,
                                            @ModelAttribute ComplaintDTO.Request request) {
        Complaint complaint = complaintService.complaintSecondhand(secondhandId, request);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("complaint", complaint);
        modelAndView.setViewName("thyme/user/login");
        return modelAndView;
    }

    // 공동구매 신고 처리
    @PostMapping("/groupBuying/{groupBuyingId}")
    public ModelAndView complaintGroupBuying(@PathVariable("groupBuyingId") Long groupBuyingId,
                                             @ModelAttribute ComplaintDTO.Request request) {
        Complaint complaint = complaintService.complaintGroupBuying(groupBuyingId, request);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("complaint", complaint);
        modelAndView.setViewName("thyme/user/login");
        return modelAndView;
    }

    // 신고 조회
    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<List<Integer>>> getMyComplaint(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(complaintService.getTags(memberId)));
    }


    // 신고 카운트
}
