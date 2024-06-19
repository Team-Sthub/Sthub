package com.ssd.sthub.controller;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import com.ssd.sthub.dto.gComment.GCommentResponseDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.GCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/groupBuying")
@RequiredArgsConstructor
public class GCommentController {
    private final GCommentService gCommentService;

    // 공동구매 게시글 댓글 작성
    @PostMapping("/{groupBuyingId}/comment")
    public ModelAndView createComment(@SessionAttribute Long memberId, @ModelAttribute @Validated GCommentRequestDto.request request, @PathVariable Long groupBuyingId) {
        GComment gComment = gCommentService.createGComment(memberId, request);
        return new ModelAndView("redirect:/groupBuying/" + request.getGroupBuyingId());
    }

    // 공동구매 게시글 댓글 전체 조회
    @GetMapping("/{groupBuyingId}/comment")
    public ResponseEntity<SuccessResponse<List<GCommentResponseDto.GCommentDto>>> getComments(@PathVariable Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(gCommentService.getGCommentList(groupBuyingId)));
    }
}
