package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.SComment;
import com.ssd.sthub.domain.Secondhand;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.SCommentRepository;
import com.ssd.sthub.repository.SecondhandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SCommentService {
    private final SCommentRepository sCommentRepository;
    private final MemberRepository memberRepository;
    private final SecondhandRepository secondhandRepository;

    // 중고거래글 댓글 작성
    public SCommentDTO.Response createComment(Long memberId, SCommentDTO.Request request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));

        Secondhand secondhand = secondhandRepository.findById(request.getSecondhandId())
                .orElseThrow(() -> new EntityNotFoundException("중고거래 게시글 조회에 실패했습니다."));

        SComment sComment = SComment.builder()
                .member(member)
                .secondhand(secondhand)
                .content(request.getContent())
                .build();

        return new SCommentDTO.Response(sCommentRepository.save(sComment));
    }
}
