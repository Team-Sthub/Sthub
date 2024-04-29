package com.ssd.sthub.controller;


import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.GroupBuyingQueryService;
import com.ssd.sthub.service.GroupBuyingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class GroupBuyingController {

    private final GroupBuyingService groupBuyingService;
    private final GroupBuyingQueryService groupBuyingQueryService;

    // 공동구매 전체 조회
    @GetMapping("/groupBuying/list/{category}")
    public ResponseEntity<SuccessResponse<GroupBuyingListDTO>> getAllGroupBuying(@PathVariable Category category, @RequestParam int pageNum) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingQueryService.getAllGroupBuying(category, pageNum)));
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정)
    @GetMapping("/groupBuying/detail")
    public ResponseEntity<SuccessResponse<GroupBuying>> getGroupBuying(@RequestHeader Long memberId, Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingQueryService.getGroupBuying(memberId, groupBuyingId)));
    }

    // 공동구매 게시글 작성
    @PostMapping("/groupBuying/create")
    public ResponseEntity<SuccessResponse<GroupBuying>> postGroupBuying(@RequestHeader Long memberId, @RequestBody GroupBuyingDetailDTO groupBuyingDetailDTO) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.postGroupBuying(memberId, groupBuyingDetailDTO)));
    }

    // 공동구매 게시글 수정
    @PatchMapping("/groupBuying/update")
    public ResponseEntity<SuccessResponse<GroupBuying>> updateGroupBuying(@RequestHeader Long memberId, @RequestBody GroupBuyingDetailDTO groupBuyingDetailDTO) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.updateGroupBuying(memberId, groupBuyingDetailDTO)));
    }

    // 공동구매 게시글 삭제
    @DeleteMapping("/groupBuying/delete")
    public ResponseEntity<SuccessResponse<String>> updateGroupBuying(@RequestHeader Long memberId, Long groupBuyingId) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingService.deleteGroupBuying(memberId, groupBuyingId)));
    }

    // 마이페이지 - 공구 모집 조회
    @GetMapping("/groupbuying/mylist")
    public ResponseEntity<SuccessResponse<GroupBuyingListDTO>> getAllGroupBuyingByMemberId(@RequestHeader Long memberId, @RequestParam int pageNuM) {
        return ResponseEntity.ok(SuccessResponse.create(groupBuyingQueryService.getAllGroupBuyingByMemberId(memberId, pageNuM)));
    }

}
