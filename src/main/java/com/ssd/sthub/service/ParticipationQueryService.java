package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.repository.ParticipationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationQueryService {
    private final ParticipationRepository participationRepository;

    // 신청폼 조회
    public Participation getParticipation(Long participationId) {
        return participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("구매 내역 조회에 실패했습니다."));
    }

    // 참여자 리스트 조회
    public Page<Participation> getParticipationList(int pageNum, GroupBuying groupBuying) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        return participationRepository.findAllByGroupBuying(groupBuying, pageRequest);
    }

    // 내가 참여한 공동구매 리스트
    public Page<Participation> getMyParticipationList(int pageNum, Long memberId) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        return participationRepository.findAllByMemberId(memberId, pageRequest);
    }
}
