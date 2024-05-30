package com.ssd.sthub.controller;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import com.ssd.sthub.dto.gComment.GCommentResponseDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.GCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/detail/comment")
    public ModelAndView createComment(@SessionAttribute Long memberId, @ModelAttribute GCommentRequestDto.request request) {
        GComment gComment = gCommentService.createGComment(memberId, request);
        return new ModelAndView("redirect:/groupBuying/detail?groupBuyingId=" + request.getGroupBuyingId());
    }

    // 공동구매 게시글 댓글 전체 조회
    @GetMapping("/detail/comment")
    public ResponseEntity<SuccessResponse<List<GCommentResponseDto.GCommentDto>>> getComments(@RequestParam Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(gCommentService.getGCommentList(groupBuyingId)));
    }
}
