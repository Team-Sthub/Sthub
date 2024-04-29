package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.SecondhandQueryService;
import com.ssd.sthub.service.SecondhandService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secondhand")
@RequiredArgsConstructor
public class SecondhandController {
    private final SecondhandService secondhandService;
    private final SecondhandQueryService secondhandQService;

    // 중고거래 게시글 생성
    public ResponseEntity<SuccessResponse<Secondhand>> createSecondhand(@RequestHeader("memberId") Long memberId, @RequestBody SecondhandDTO.Request request) {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.createSecondhand(memberId, request)));
    }

    // 중고거래 게시글 수정

    // 중고거래 게시글 삭제
    public ResponseEntity<SuccessResponse<String>> deleteSecondhand(@RequestHeader("memberId") Long memberId, @RequestParam Long secondhandId) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(secondhandService.deleteSecondhand(memberId, secondhandId)));
    }

    // 중고거래 게시글 상세 조회

    // 중고거래 게시글 전체 조회
}
