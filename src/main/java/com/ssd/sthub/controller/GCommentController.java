package com.ssd.sthub.controller;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import com.ssd.sthub.dto.gComment.GCommentResponseDto;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.GCommentQueryService;
import com.ssd.sthub.service.GCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupBuying")
@RequiredArgsConstructor
public class GCommentController {
    private final GCommentService gCommentService;
    private final GCommentQueryService gCommentQueryService;

    // 공동구매 게시글 댓글 작성
    @PostMapping("/detail/comment")
    public ResponseEntity<SuccessResponse<GComment>> createComment(@RequestHeader Long memberId, Long groupBuyingId, @RequestBody GCommentRequestDto.request request) {
        return ResponseEntity.ok(SuccessResponse.create(gCommentService.createGComment(memberId, groupBuyingId, request)));
    }

    // 공동구매 게시글 댓글 전체 조회
    @GetMapping("/detail/comment")
    public ResponseEntity<SuccessResponse<List<GCommentResponseDto.GCommentDto>>> getComments(@RequestParam Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(gCommentQueryService.getGCommentList(groupBuyingId)));
    }
}
