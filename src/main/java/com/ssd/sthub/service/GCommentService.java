package com.ssd.sthub.service;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.dto.gComment.GCommentRequestDto;
import com.ssd.sthub.repository.GCommentRepository;
import com.ssd.sthub.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GCommentService {

    private final GCommentRepository gCommentRepository;
    private final MemberRepository memberRepository;

    // 공동구매 댓글 작성
    public GComment createGComment(Long memberId, GroupBuying groupBuying, GCommentRequestDto.request request) throws NullPointerException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

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
}