package com.ssd.sthub.service;

import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
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
@Transactional
@RequiredArgsConstructor
public class GroupBuyingService {

    public final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;

    // 공동구매 게시글 작성
    public GroupBuying postGroupBuying(Long memberId, PostGroupBuyingDTO postGroupBuyingDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        GroupBuying groupBuying = new GroupBuying(postGroupBuyingDTO, member);
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 수정
    public GroupBuying updateGroupBuying(Long memberId, GroupBuyingDetailDTO groupBuyingDetailDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingDetailDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.")
        );

        groupBuying.updateGroupBuying(groupBuyingDetailDTO);
        return groupBuyingRepository.save(groupBuying);
    }

    // 공동구매 게시글 삭제
    public String deleteGroupBuying(Long memberId, Long groupBuyingId) {
        Optional<GroupBuying> findGroupBuying = groupBuyingRepository.findById(groupBuyingId);
        if (findGroupBuying == null) {
            new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.");
        }
        if(findGroupBuying.get().getMember().getId() != memberId) {
            new EntityNotFoundException("해당 공동구매 게시글 작성자와 현재 유저가 다릅니다.");
        }
        groupBuyingRepository.deleteById(groupBuyingId);
        return "삭제 완료되었습니다.";
    }

    // 공동구매 전체 조회 (페이징 포함)
    public GroupBuyingListDTO getAllGroupBuying(Category category, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<GroupBuying> groupBuyings;

        if (category == Category.ALL)
            groupBuyings = groupBuyingRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        else {
            groupBuyings = groupBuyingRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageRequest);
        }

        List<GroupBuyingListDTO.GroupBuyingDTO> groupBuyingListDto = groupBuyings.stream()
                .map(groupBuying -> new GroupBuyingListDTO.GroupBuyingDTO(groupBuying.getId(), groupBuying.getTitle(), groupBuying.getPrice(),
                        groupBuying.getImageUrl(), groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDTO(groupBuyingListDto, groupBuyings.getTotalPages());
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정) + 수락 여부에 따라 오픈채팅 링크 공개여부 달라짐
    public GroupBuying getGroupBuying(Long memberId, Long groupBuyingId) throws NullPointerException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId).orElseThrow(
                () -> new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.")
        );

        return groupBuying;
    }

    // 마이페이지 - 공구 모집 조회 (페이징 포함)
    public GroupBuyingListDTO getAllGroupBuyingByMemberId(Long memberId, int pageNum) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );

        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<GroupBuying> groupBuyings = groupBuyingRepository.findAllByMemberId(memberId, pageRequest);
        List<GroupBuyingListDTO.GroupBuyingDTO> groupBuyingListDto = groupBuyings.stream()
                .map(groupBuying -> new GroupBuyingListDTO.GroupBuyingDTO(groupBuying.getId(), groupBuying.getTitle(), groupBuying.getPrice(),
                        groupBuying.getImageUrl(), groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDTO(groupBuyingListDto, groupBuyings.getTotalPages());
    }
}
