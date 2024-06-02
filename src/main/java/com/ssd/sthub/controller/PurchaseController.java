package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    // 중고거래 결제 (구매)
    @PostMapping("")
    public ModelAndView createPurchase(@SessionAttribute Long memberId, @RequestParam Long secondhandId) {
        boolean result = purchaseService.createPurchase(memberId, secondhandId);
        return new ModelAndView("redirect:/secondhand/detail?secondhandId=" + secondhandId);
    }

    // 구매 내역 전체 조회
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<Page<Secondhand>>> getPurchaseSecondhands(@RequestHeader Long memberId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(purchaseService.getPurchaseSecondhands(memberId, pageNum)));
    }

    // 구매내역 상위 4개 조회
    @GetMapping("/list/top4List")
    public String getPurchaseSecondhands(@SessionAttribute(name = "memberId") Long memberId, Model model) throws BadRequestException {
        List<SecondhandDTO.Top4ListResponse> secondhandList = purchaseService.getTop4Items(memberId);
        model.addAttribute("secondhandList", secondhandList);
        return "thyme/user/fragments/purchaseFragments";
    }

    // 구매내역 배송조회 (open api 사용 예정)
}
