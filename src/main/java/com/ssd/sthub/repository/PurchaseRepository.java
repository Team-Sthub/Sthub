package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Secondhand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findById(Long purchaseId);

    Purchase save(Purchase purchase);

    Page<Secondhand> findAllSecondhandByMemberId(Long memberId, PageRequest pageRequest);
}
