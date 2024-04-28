package com.ssd.sthub.service;

import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;

    // 공동구매 신청서 작성
    public Participation createParticipation(Participation participation) throws NullPointerException{
        if (participation == null) {
            throw new NullPointerException("신청폼 작성에 실패했습니다.");
        }

        return participationRepository.save(participation);
    }

    // 공동구매 신청자 수락/거절
    public Participation accpetMember(Participation participation) {
        // 수락하면 participation의 accept를 1로 수정
        // 거절하면 participation의 accept를 2로 수정
        return participationRepository.save(participation);
    }
}
