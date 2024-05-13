package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationResponseDto;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.ParticipationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationQueryService {
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    // 신청폼 조회
    public Participation getParticipation(Long participationId) {
        return participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("구매 내역 조회에 실패했습니다."));
    }

    // 참여자 리스트 조회
    public Page<Participation> getParticipationList(Long groupBuyingId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 6);
        Page<Participation> participations = participationRepository.findAllByGroupBuyingId(groupBuyingId, pageRequest);

        if(participations == null || participations.isEmpty())
            throw new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다.");

        return participations;
    }

    // 내가 참여한 공동구매 리스트
    public ParticipationResponseDto getMyParticipationList(int pageNum, Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<Participation> participations = participationRepository.findAllByMemberId(memberId, pageRequest);
        List<ParticipationResponseDto.ParticipationDto> participationListDto = participations.stream()
                .map(participation -> new ParticipationResponseDto.ParticipationDto(participation.getId(),
                        participation.getMember().getNickname(), participation.getMember().getPhone(),
                        participation.getContent(), participation.getAccept()))
                .collect(Collectors.toList());

        return new ParticipationResponseDto(participationListDto, participations.getTotalPages());
    }
}
