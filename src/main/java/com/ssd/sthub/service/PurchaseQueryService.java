package com.ssd.sthub.service;

import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.repository.PurchaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseQueryService {
    private final PurchaseRepository purchaseRepository;

    // 구매 상세 조회
    public Purchase getPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("구매 내역 조회에 실패했습니다."));
    }

    // 구매 내역 전체 조회
    public Page<Secondhand> getPurchaseSecondhands(Long memberId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        return purchaseRepository.findAllSecondhandByMemberId(memberId, pageRequest);
    }
}
