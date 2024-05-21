package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ParticipationService;
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

    // 참여 신청폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm() {return "thyme/participation/create";}

    // 참여 신청폼 작성
    @PostMapping("/create")
    public ModelAndView createParticipation(@RequestParam Long memberId, @RequestParam Long groupBuyingId, @ModelAttribute ParticipationRequestDto.request request) {
        Participation participation = participationService.createParticipation(memberId, groupBuyingId, request);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("participation", participation);
        return new ModelAndView("redirect:/participation/list?pageNum=0&groupBuyingId="+groupBuyingId);
    }

    // 참여 신청폼 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<Participation>> getParticipation(@RequestParam Long participationId) {
        return ResponseEntity.ok(SuccessResponse.create(participationService.getParticipation(participationId)));
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
    public ResponseEntity<SuccessResponse<Participation>> updateParticipation(@RequestHeader Long memberId, @RequestBody ParticipationRequestDto.PatchRequest request) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.updateParticipation(memberId, request)));
    }

    // 참여 신청폼 수락/거절
    @PatchMapping("/list")
    public ModelAndView patchParticipations(@RequestHeader Long memberId, @RequestParam Long participationId, @RequestBody ParticipationRequestDto.AcceptRequest request) throws BadRequestException {
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
