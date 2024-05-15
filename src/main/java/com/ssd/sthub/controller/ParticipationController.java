package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {
    private final ParticipationService participationService;

    // 참여 신청폼으로 이동

    // 참여 신청폼 작성
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<Participation>> createParticipation(@RequestHeader Long memberId, Long groupBuyingId, @RequestBody ParticipationRequestDto.request request) {
        return ResponseEntity.ok(SuccessResponse.create(participationService.createParticipation(memberId, groupBuyingId, request)));
    }

    // 참여 신청폼 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<Participation>> getParticipation(@RequestParam Long participationId) {
        return ResponseEntity.ok(SuccessResponse.create(participationService.getParticipation(participationId)));
    }

    // 참여 신청폼 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<Page<Participation>>> getParticipations(@RequestParam int pageNum, Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(participationService.getParticipationList(groupBuyingId, pageNum)));
    }

    // 참여 신청폼 수정
    @PatchMapping("/update")
    public ResponseEntity<SuccessResponse<Participation>> updateParticipation(@RequestHeader Long memberId, @RequestBody ParticipationRequestDto.PatchRequest request) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.updateParticipation(memberId, request)));
    }

    // 참여 신청폼 수락/거절
    @PatchMapping("/list")
    public ResponseEntity<SuccessResponse<Participation>> patchParticipations(@RequestHeader Long memberId, @RequestParam Long participationId, @RequestBody ParticipationRequestDto.AcceptRequest request) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.accpetMember(memberId,participationId, request)));
    }

    //마이페이지 - 공구 참여 전체 조회
    @GetMapping("/mylist")
    public ResponseEntity<SuccessResponse<Page<Participation>>> getParticipationGroupBuyings(@RequestHeader Long memberId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationService.getMyParticipationList(pageNum, memberId)));
    }
}
