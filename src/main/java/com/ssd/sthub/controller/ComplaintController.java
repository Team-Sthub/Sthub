package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Complaint;
import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.dto.complaint.ComplaintDTO;
import com.ssd.sthub.dto.review.ReviewRequestDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.ComplaintService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/complaint")
public class ComplaintController {
    private final ComplaintService complaintService;

    // 신고 작성폼으로 이동
    @GetMapping("/moveToForm")
    public String showCreateForm(Model model) {
        return "thyme/complaint/create";
    }

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
    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<List<Integer>>> getMyComplaint(@RequestHeader Long memberId) {
        return ResponseEntity.ok(SuccessResponse.create(complaintService.getTags(memberId)));
    }


    // 신고 카운트
}
