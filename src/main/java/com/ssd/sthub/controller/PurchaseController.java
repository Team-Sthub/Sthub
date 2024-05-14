package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    // 중고거래 결제 (구매)
    @PostMapping("")
    public ResponseEntity<SuccessResponse<Boolean>> createPurchase(@RequestHeader Long memberId, @RequestParam Long secondhandId) {
        return ResponseEntity.ok(SuccessResponse.create(purchaseService.createPurchase(memberId, secondhandId)));
    }

    // 구매 내역 전체 조회
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<Page<Secondhand>>> getPurchaseSecondhands(@RequestHeader Long memberId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(purchaseService.getPurchaseSecondhands(memberId, pageNum)));
    }

    // 구매내역 배송조회 (open api 사용 예정)
}
