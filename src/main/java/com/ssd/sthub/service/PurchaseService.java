package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Purchase;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.secondhand.SecondhandDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.PurchaseRepository;
import com.ssd.sthub.repository.PurchaseRepositoryImpl;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseRepositoryImpl purchaseRepositoryImpl;
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

        secondhand.updateStatus("예약중"); // 예약중으로 상태 변경
        secondhandRepository.save(secondhand);
        purchaseRepository.save(purchase);
        return true;
    }

    // 구매 상세 조회
    public Purchase getPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException("구매 내역 조회에 실패했습니다."));
    }

    // 마이페이지 - 구매 내역 전체 조회 (페이징 포함)
    public List<SecondhandDTO.MyAllListResponse> getPurchaseSecondhands(Long memberId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<Secondhand> purchases = purchaseRepositoryImpl.findSecondhandsByMemberId(memberId, pageRequest);

        return purchases.stream()
                .map(g -> new SecondhandDTO.MyAllListResponse(g, g.getId(), g.getImageList(), purchases.getTotalPages(), pageNum + 1))
                .collect(Collectors.toList());
    }

    // 마이페이지 - 구매 내역에서 후기 조회
    public Optional<Purchase> findPurchaseBySecondhandId(Long secondhandId) {
        return purchaseRepository.findBySecondhandId(secondhandId);
    }

    // 구매 내역 상위 4개 조회
    public List<SecondhandDTO.Top4ListResponse> getTop4Items(Long memberId) {
        List<Secondhand> secondhands = purchaseRepositoryImpl.findTop4Secondhands(memberId);
        return secondhands.stream()
                .map(s->new SecondhandDTO.Top4ListResponse(s, s.getImageList()))
                .collect(Collectors.toList());
    }
}
