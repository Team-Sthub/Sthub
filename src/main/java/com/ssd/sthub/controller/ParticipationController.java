package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ParticipationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {
    private final ParticipationService participationService;
    private final HttpServletRequest httpServletRequest;

    // 참여 신청폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm() {return "thyme/participation/create";}

    // 참여 신청폼 작성
    @PostMapping("/create")
    public ModelAndView createParticipation(@SessionAttribute(name = "memberId") Long memberId, Long groupBuyingId, @ModelAttribute ParticipationRequestDto.request request) {
        Participation participation = participationService.createParticipation(memberId, groupBuyingId, request);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("participation", participation);
        return new ModelAndView("redirect:/participation/detail?participationId="+participation.getId());
    }

    // 참여 신청폼 상세 조회
    @GetMapping("/detail")
    public ModelAndView getParticipation(@RequestParam Long participationId) {
        Participation participation = participationService.getParticipation(participationId);
        ModelAndView modelAndView = new ModelAndView("thyme/participation/detail");
        modelAndView.addObject("participation", participation);
        return modelAndView;
    }

    // 참여 신청폼 리스트 조회
    @GetMapping("/list")
    public ModelAndView getParticipations(@RequestParam int pageNum, @RequestParam Long groupBuyingId) {
        Page<Participation> participationList = participationService.getParticipationList(groupBuyingId, pageNum);
        ModelAndView modelAndView = new ModelAndView("thyme/participation/list");
        modelAndView.addObject("participationList", participationList);
        return modelAndView;
    }

    // 참여 신청폼 수정
    @PatchMapping("/update")
    public ResponseEntity<SuccessResponse<Participation>> updateParticipation(@SessionAttribute(name = "memberId") Long memberId, @RequestBody ParticipationRequestDto.PatchRequest request) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.updateParticipation(memberId, request)));
    }

    // 참여 신청폼 수락/거절
    @PatchMapping("/list")
    public ModelAndView patchParticipations(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long participationId, @RequestBody ParticipationRequestDto.AcceptRequest request) throws BadRequestException {
        Long groupBuyingId = request.getGroupBuyingId();
        participationService.accpetMember(memberId, participationId, request);

        Page<Participation> participationList = participationService.getParticipationList(groupBuyingId, 1);
        ModelAndView modelAndView = new ModelAndView("thyme/participation/list");
        modelAndView.addObject("participationList", participationList);
        return modelAndView;
    }

    //마이페이지 - 공구 참여 전체 조회
    @GetMapping("/mylist")
    public ResponseEntity<SuccessResponse<Page<Participation>>> getParticipationGroupBuyings(@RequestHeader Long memberId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.getMyParticipationList(pageNum, memberId)));
    }
}
