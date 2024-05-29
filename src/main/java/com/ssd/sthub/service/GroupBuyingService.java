package com.ssd.sthub.service;

import com.ssd.sthub.domain.GImage;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.enumerate.Category;
import com.ssd.sthub.dto.groupBuying.GroupBuyingDetailDTO;
import com.ssd.sthub.dto.groupBuying.GroupBuyingListDTO;
import com.ssd.sthub.dto.groupBuying.PostGroupBuyingDTO;
import com.ssd.sthub.repository.GImageRepository;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyingService {

    public final GroupBuyingRepository groupBuyingRepository;
    public final MemberRepository memberRepository;
    public final GImageRepository gImageRepository;
    private final AWSS3SService awss3SService;

    // 공동구매 게시글 작성
    public PostGroupBuyingDTO.Response postGroupBuying(Long memberId, List<String> imgUrls, PostGroupBuyingDTO.Request postGroupBuyingDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        GroupBuying groupBuying = new GroupBuying(postGroupBuyingDTO, member);
        groupBuyingRepository.save(groupBuying);

        if(imgUrls != null && !imgUrls.isEmpty()) {
            for (String imgUrl : imgUrls) {
                GImage gImage = GImage.builder()
                        .path(imgUrl)
                        .groupBuying(groupBuying)
                        .build();
                gImageRepository.save(gImage);
            }
        }

        log.info("게시글 저장 : ", groupBuying.getId());
        GroupBuying checkGroupBuying = groupBuyingRepository.findById(groupBuying.getId()).orElseThrow(
                () -> new EntityNotFoundException("중고거래 게시글 저장에 실패하였습니다."));

        return new PostGroupBuyingDTO.Response(groupBuying, gImageRepository.findAllByGroupBuying(groupBuying));
    }

    // 공동구매 게시글 수정
    public GroupBuyingDetailDTO.PatchResponse updateGroupBuying(Long memberId, List<String> imgUrls, GroupBuyingDetailDTO.PatchRequest groupBuyingDetailDTO) throws BadRequestException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingDetailDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.")
        );

        if(!groupBuying.getMember().getId().equals(memberId))
            throw new BadRequestException("작성자만 수정할 수 있습니다.");

        awss3SService.deleteImages(
                gImageRepository.findAllByGroupBuying(groupBuying)
                        .stream().map(GImage::getPath)
                        .collect(Collectors.toList())
        );
        gImageRepository.deleteAllByGroupBuying(groupBuying);

        groupBuying.updateGroupBuying(groupBuyingDetailDTO);
        groupBuying = groupBuyingRepository.save(groupBuying);

        for(String imgUrl : imgUrls) {
            GImage gImage = GImage.builder()
                    .path(imgUrl)
                    .groupBuying(groupBuying)
                    .build();
            gImageRepository.save(gImage);
        }

        List<GImage> gImages = gImageRepository.findAllByGroupBuying(groupBuying);
        return new GroupBuyingDetailDTO.PatchResponse(groupBuying, gImages, groupBuying.getCommentList());

    }

    // 공동구매 게시글 삭제
    public String deleteGroupBuying(Long memberId, Long groupBuyingId) {
        Optional<GroupBuying> findGroupBuying = groupBuyingRepository.findById(groupBuyingId);
        if (findGroupBuying == null) {
            new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.");
        }
        if(findGroupBuying.get().getMember().getId() != memberId) {
            new BadRequestException("해당 공동구매 게시글 작성자와 현재 유저가 다릅니다.");
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
                .map(groupBuying -> new GroupBuyingListDTO.GroupBuyingDTO(groupBuying.getId(), groupBuying.getTitle(), groupBuying.getPrice(), groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDTO(groupBuyingListDto, groupBuyings.getTotalPages());
    }

    // 공동구매 게시글(상세) 조회 (작성자 확인은 controller에서 하고 뷰 설정) + 수락 여부에 따라 오픈채팅 링크 공개여부 달라짐
    public GroupBuyingDetailDTO.Response getGroupBuying(Long memberId, Long groupBuyingId) throws NullPointerException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        GroupBuying groupBuying = groupBuyingRepository.findById(groupBuyingId).orElseThrow(
                () -> new EntityNotFoundException("해당 공동구매 게시글 조회에 실패했습니다.")
        );
        return new GroupBuyingDetailDTO.Response(groupBuying, groupBuying.getImageList(), groupBuying.getCommentList());
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
                        groupBuying.getMember().getNickname(), groupBuying.getStatus()))
                .collect(Collectors.toList());
        return new GroupBuyingListDTO(groupBuyingListDto, groupBuyings.getTotalPages());
    }

    // 회원 닉네임 조회
    public String getNickname(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원 조회에 실패했습니다.")
        );
        return member.getNickname();
    }

    // 공동구매 글 이미지 조회
    public List<String> getImageUrls(Long groupBuyingId) {
        return gImageRepository.findAllByGroupBuyingId(groupBuyingId)
                .stream()
                .map(GImage::getPath)
                .collect(Collectors.toList());
    }
}
