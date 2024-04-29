package com.ssd.sthub.controller;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ParticipationQueryService;
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
    private final ParticipationQueryService participationQueryService;

    // 신청서 작성
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<Participation>> createParticipation(@RequestHeader Long memberId, Long groupBuyingId, @RequestBody ParticipationRequestDto.request request) {
        return ResponseEntity.ok(SuccessResponse.create(participationService.createParticipation(memberId, groupBuyingId, request)));
    }

    // 신청서 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<Participation>> getParticipation(@RequestParam Long particionpationId) {
        return ResponseEntity.ok(SuccessResponse.create(participationQueryService.getParticipation(particionpationId)));
    }

    // 신청서 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<Page<Participation>>> getParticipations(@RequestParam int pageNum, Long groupBuyingId) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(participationQueryService.getParticipationList(groupBuyingId, pageNum)));
    }
}
