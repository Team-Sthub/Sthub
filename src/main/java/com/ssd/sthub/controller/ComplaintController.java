package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
public class ComplaintController {
    private final ComplaintService complaintService;

    // 중고거래 신고 처리
    @PostMapping("/{secondhandId}")
    public ResponseEntity<SuccessResponse<Complaint>> complaintSecondhand(@PathVariable("secondhandId") Long secondhandId, @RequestBody ComplaintDTO request) {
        return ResponseEntity.ok(SuccessResponse.create(complaintService.complaintSecondhand(secondhandId, request)));
    }

    // 공동구매 신고 처리
    @PostMapping("/{groupBuyingId}")
    public ResponseEntity<SuccessResponse<Complaint>> complaintGroupBuying(@PathVariable("groupBuyingId") Long groupBuyingId, @RequestBody ComplaintDTO request) {
        return ResponseEntity.ok(SuccessResponse.create(complaintService.complaintGroupBuying(groupBuyingId, request)));
    }

    // 신고 조회


    // 신고 카운트
}
