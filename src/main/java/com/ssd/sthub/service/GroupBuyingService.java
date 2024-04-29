package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyingService {

    public final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;

    // 공동구매 게시글 작성
    public GroupBuying postGroupBuying(Long memberId, GroupBuyingDetailDTO groupBuyingDetailDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException()
        );
        GroupBuying groupBuying = new GroupBuying(groupBuyingDetailDTO, member);
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 수정
    public GroupBuying updateGroupBuying(Long memberId, GroupBuyingDetailDTO groupBuyingDetailDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException()
        );
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingDetailDTO.getId()).orElseThrow(
                () -> new NullPointerException()
        );

        groupBuying.updateGroupBuying(groupBuyingDetailDTO);
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 삭제
    public String deleteGroupBuying(Long memberId, Long groupBuyingId) {
        Optional<GroupBuying> findGroupBuying = groupBuyingRepository.findById(groupBuyingId);
        if (findGroupBuying == null) {
            // exception 반환
        }
        if(findGroupBuying.get().getMember().getId() != memberId) {
            // exception 반환
        }
        groupBuyingRepository.deleteById(groupBuyingId);
        return "삭제 완료되었습니다.";
    }
}
