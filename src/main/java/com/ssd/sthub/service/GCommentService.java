package com.ssd.sthub.service;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import com.ssd.sthub.dto.gComment.GCommentResponseDto;
import com.ssd.sthub.repository.GCommentRepository;
import com.ssd.sthub.repository.GroupBuyingRepository;
import com.ssd.sthub.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GCommentService {

    private final GCommentRepository gCommentRepository;
    private final MemberRepository memberRepository;
    private final GroupBuyingRepository groupBuyingRepository;

    // 공동구매 댓글 작성
    public GComment createGComment(Long memberId, GCommentRequestDto.request request) throws NullPointerException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        GroupBuying groupBuying = groupBuyingRepository.findById(request.getGroupBuyingId())
                .orElseThrow(() -> new EntityNotFoundException("공동구매 게시글 조회에 실패했습니다."));

        GComment gComment = GComment.builder()
                .member(member)
                .groupBuying(groupBuying)
                .request(request)
                .build();

        if (gComment == null) {
            throw new NullPointerException("작성된 댓글 내용이 없습니다.");
        }

        return gCommentRepository.save(gComment);
    }

    // 공동구매 댓글 리스트 조회
    public List<GCommentResponseDto.GCommentDto> getGCommentList(Long groupBuyingId) {
        List<GComment> gComments = gCommentRepository.findAllByGroupBuyingId(groupBuyingId);

        if(gComments == null || gComments.isEmpty())
            throw new EntityNotFoundException("댓글 작성 내용이 없습니다.");


        return gComments.stream()
                .map(gComment -> new GCommentResponseDto.GCommentDto(gComment.getId(), gComment.getContent(), gComment.getMember().getNickname()))
                .collect(Collectors.toList());
    }
}