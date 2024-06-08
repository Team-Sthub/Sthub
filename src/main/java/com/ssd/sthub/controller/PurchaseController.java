package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
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

    // 마이페이지 - 구매 내역 (더보기)
    @GetMapping("/list")
    public ModelAndView getPurchaseSecondhands(@SessionAttribute(name = "memberId") Long memberId, @RequestParam int pageNum, @RequestParam(required = false) String errorMessage) {
        List<SecondhandDTO.MyAllListResponse> myPurchaseList = purchaseService.getPurchaseSecondhands(memberId, pageNum - 1);
        // Purchase 객체를 조회하여 Purchase ID를 가져오고, 뷰에 전달
        for (SecondhandDTO.MyAllListResponse item : myPurchaseList) {
            purchaseService.findPurchaseBySecondhandId(item.getPurchaseId())
                    .ifPresent(purchase -> item.setPurchaseId(purchase.getId()));
        }
        ModelAndView modelAndView = new ModelAndView("thyme/purchase/myPurchase");
        modelAndView.addObject("myPurchaseList",myPurchaseList);
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    // 마이페이지 - 구매 내역 (4개)
    @GetMapping("/list/top4List")
    public String getPurchaseSecondhands(@SessionAttribute(name = "memberId") Long memberId, Model model) {
        List<SecondhandDTO.Top4ListResponse> secondhandList = purchaseService.getTop4Items(memberId);
        model.addAttribute("secondhandList", secondhandList);
        return "thyme/user/fragments/purchaseFragments";
    }

    // 구매내역 배송조회 (open api 사용 예정)
}
