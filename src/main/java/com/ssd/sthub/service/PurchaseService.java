package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final SecondhandRepository secondhandRepository;

    // 구매 저장
    public boolean createPurchase(Long memberId, Long secondhandId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = secondhandRepository.findById(secondhandId)
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        Purchase purchase = Purchase.builder()
                    .secondhand(secondhand)
                    .member(member)
                    .build();

        purchaseRepository.save(purchase);
        return true;
    }


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
