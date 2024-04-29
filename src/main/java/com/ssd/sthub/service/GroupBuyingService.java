package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
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

    // 공동구매 게시글 작성 (추후에 dto에 내용+작성자까지 넣어서 저장해야함)
    public GroupBuying postGroupBuying(Long memberId, GroupBuying groupBuying) {
        if (groupBuying == null) {
            // exception 반환
        }
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 수정 (추후에 dto에 수정된 내용+작성자까지 넣어서 저장해야함)
    public GroupBuying updateGroupBuying(Long memberId, GroupBuying groupBuying) {
        if (groupBuying == null) {
            // exception 반환
        }
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 삭제
    public String deleteGroupBuying(Long memberId, Long groupBuyingId) {
        Optional<GroupBuying> findGroupBuying = groupBuyingRepository.findById(groupBuyingId);
        if (findGroupBuying == null) {
            // exception 반환
        }
        groupBuyingRepository.deleteById(groupBuyingId);
        return "삭제 완료되었습니다.";
    }
}
