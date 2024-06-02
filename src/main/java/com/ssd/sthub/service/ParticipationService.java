package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Participation;
import com.ssd.sthub.dto.participation.ParticipationRequestDto;
import com.ssd.sthub.dto.participation.ParticipationResponseDto;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.ParticipationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 공동구매 신청서 작성
    public Participation createParticipation(Long memberId, Long groupBuyingId, ParticipationRequestDto.Request request) throws NullPointerException{
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId)
                .orElseThrow(() -> new EntityNotFoundException("공동구매 게시글 조회에 실패했습니다."));

        Participation participation = Participation.builder()
                .member(member)
                .groupBuying(groupBuying)
                .request(request)
                .build();
        participationRepository.save(participation);
        return participation;
    }

    // 공동구매 신청자 수락/거절
    public Participation accpetMember(Long memberId, Long participationId, ParticipationRequestDto.AcceptRequest request) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        GroupBuying groupBuying = groupBuyingRepository.findById(request.getGroupBuyingId())
                .orElseThrow(() -> new EntityNotFoundException("공동구매 게시글 조회에 실패했습니다."));

        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("공동구매 신청서 조회에 실패했습니다."));

        if(!groupBuying.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 수락/거절 할 수 있습니다.");

        if (request.getAccept() == 1 || request.getAccept() == 2) {
            participation.accept(request);
        } else {
            throw new BadRequestException("잘못된 값입니다.");
        }

        return participationRepository.save(participation);
    }

    // 신청폼 조회
    public Participation getParticipation(Long participationId) {
        return participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("구매 내역 조회에 실패했습니다."));
    }

    // 신청폼 수정
    public Participation updateParticipation(Long memberId, Long participationId, ParticipationRequestDto.PatchRequest request) throws BadRequestException{
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("신청서 조회에 실패했습니다."));

        if(!participation.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 수정할 수 있습니다.");
        participation.update(request);
        return participationRepository.save(participation);
    }

    // 참여자 리스트 조회
    public List<ParticipationResponseDto.ParticipationList> getParticipationList(Long groupBuyingId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 6);
        Page<Participation> participations = participationRepository.findAllByGroupBuyingId(groupBuyingId, pageRequest);

        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId)
                .orElseThrow(() -> new EntityNotFoundException("공동구매 게시글 조회에 실패했습니다."));

        return participations.stream()
                .map(p->new ParticipationResponseDto.ParticipationList(p, participations.getTotalPages(), pageNum + 1))
                .collect(Collectors.toList());
    }

    // 내가 참여한 공동구매 리스트
    public List<ParticipationResponseDto.ParticipationList> getMyParticipationList(int pageNum, Long memberId) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<Participation> participations = participationRepository.findAllByMember(member, pageRequest);

        if (participations == null || participations.isEmpty())
            throw new BadRequestException("공동구매에 참여하지 않았습니다.");

        return participations.stream()
                .map(p -> new ParticipationResponseDto.ParticipationList(p, participations.getTotalPages(), pageNum))
                .collect(Collectors.toList());
    }

    // 내가 참여한 공동구매 리스트 4개
    public List<ParticipationResponseDto.ParticipationDto> getParticipationMylist(Long memberId) throws BadRequestException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        List<Participation> participations = participationRepository.findAllByMember(member);
        participations.sort(Comparator.comparing(Participation::getCreatedAt).reversed());

        return participations.stream()
                .filter(participation -> participation.getAccept() == 1) // Filter for accept == 1
                .limit(4)
                .map(ParticipationResponseDto.ParticipationDto::new)
                .collect(Collectors.toList());
    }

    //공동구매 작성자 확인
    public boolean isGroupBuyingWriter(Long memberId, Long groupBuyingId) {
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId)
                .orElseThrow(() -> new EntityNotFoundException("공동구매 게시글 조회에 실패했습니다."));

        return groupBuying.getMember().getId().equals(memberId);
    }

    // 신청서 작성자 확인
    public boolean isParticipationWriter(Long memberId, Long participationId) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new EntityNotFoundException("신청서 조회에 실패했습니다."));

        return participation.getMember().getId().equals(memberId);
    }
}
