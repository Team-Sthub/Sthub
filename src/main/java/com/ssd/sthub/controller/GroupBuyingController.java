package com.ssd.sthub.controller;


import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.GroupBuyingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/groupBuying")
public class GroupBuyingController {

    private final GroupBuyingService groupBuyingService;

    // 공동구매 전체 조회
    @GetMapping("/list/{category}")
    public ResponseEntity<SuccessResponse<GroupBuyingListDTO>> getAllGroupBuying(@PathVariable Category category, @RequestParam int pageNum) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.getAllGroupBuying(category, pageNum)));
    }

    // 공동구매 게시글 상세 조회 (일단 뷰 확인용)
//    @GetMapping("/detail")
//    public String showDetail() {
//        return "thyme/groupBuying/detail";
//    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정) 작성자 수락 여부 확인 후 링크 공개 및 미공개 해야함.
    @GetMapping("/detail")
    public ModelAndView getGroupBuying(@SessionAttribute(name = "memberId") Long memberId, @RequestParam Long groupBuyingId) {
        GroupBuying groupBuying = groupBuyingService.getGroupBuying(memberId, groupBuyingId);
//        if(memberId == groupBuying.getMember().getId())
            return new ModelAndView("thyme/groupBuying/detail", "groupBuying", groupBuying);
    }

    // 공동구매 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<GroupBuying>> postGroupBuying(@RequestHeader Long memberId, @RequestBody PostGroupBuyingDTO postGroupBuyingDTO) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.postGroupBuying(memberId, postGroupBuyingDTO)));
    }

    // 공동구매 게시글 수정
    @PatchMapping("/update")
    public ResponseEntity<SuccessResponse<GroupBuying>> updateGroupBuying(@RequestHeader Long memberId, @RequestBody GroupBuyingDetailDTO groupBuyingDetailDTO) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.updateGroupBuying(memberId, groupBuyingDetailDTO)));
    }

    // 공동구매 게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse<String>> updateGroupBuying(@RequestHeader Long memberId, Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.deleteGroupBuying(memberId, groupBuyingId)));
    }

    // 마이페이지 - 공구 모집 조회
    @GetMapping("/mylist")
    public ResponseEntity<SuccessResponse<GroupBuyingListDTO>> getAllGroupBuyingByMemberId(@RequestHeader Long memberId, @RequestParam int pageNuM) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.getAllGroupBuyingByMemberId(memberId, pageNuM)));
    }
}
