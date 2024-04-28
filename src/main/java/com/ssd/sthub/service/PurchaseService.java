package com.ssd.sthub.service;

import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    // 구매 저장
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
