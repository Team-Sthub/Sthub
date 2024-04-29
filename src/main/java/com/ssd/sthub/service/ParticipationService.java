package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.ParticipationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    // 공동구매 신청서 작성
    public Participation createParticipation(Long memberId, GroupBuying groupBuying, ParticipationRequestDto.request request) throws NullPointerException{
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Participation participation = Participation.builder()
                .member(member)
                .groupBuying(groupBuying)
                .request(request)
                .build();

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
