package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDto;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyingQueryService {

    public final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;

    // 공동구매 전체 조회 (페이징 포함), 마이페이지 - 공구 참여 모집&참여 상세 조회
    public GroupBuyingListDto getAllGroupBuying(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<GroupBuying> groupBuyings = groupBuyingRepository.findAll(pageRequest);
        List<GroupBuyingListDto.GroupBuyingDto> groupBuyingListDto = groupBuyings.stream()
                .map(groupBuying -> new GroupBuyingListDto.GroupBuyingDto(groupBuying.getId(), groupBuying.getTitle(), groupBuying.getPrice(),
                        groupBuying.getImageUrl(), groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDto(groupBuyingListDto, groupBuyings.getTotalPages());
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정)
    public Optional<GroupBuying> getGroupBuying(Long groupBuyingId) throws NullPointerException {
        Optional<GroupBuying> groupBuying = groupBuyingRepository.findById(groupBuyingId);
        if(groupBuying == null) {
            // exception 반환
        }

        return groupBuying;
    }

    // 마이페이지 - 공구 모집 조회 (페이징 포함)
    public GroupBuyingListDto getAllGroupBuyingByMemberId(Long memberId, int pageNum) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member == null) {
            // exception 반환
        }

        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<GroupBuying> groupBuyings = groupBuyingRepository.findAllByMemberId(memberId, pageRequest);
        List<GroupBuyingListDto.GroupBuyingDto> groupBuyingListDto = groupBuyings.stream()
                .map(groupBuying -> new GroupBuyingListDto.GroupBuyingDto(groupBuying.getId(), groupBuying.getTitle(), groupBuying.getPrice(),
                        groupBuying.getImageUrl(), groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDto(groupBuyingListDto, groupBuyings.getTotalPages());
    }

}
