package com.ssd.sthub.service;

import com.ssd.sthub.domain.SComment;
import com.ssd.sthub.repository.SCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SCommentQueryService {
    private final SCommentRepository sCommentRepository;

    // 중고거래 게시글 댓글 전체 조회
    public List<SComment> getComments(Long secondhandId) {
        List<SComment> comments = sCommentRepository.findAllBySecondHandId(secondhandId);

        if(comments == null || comments.isEmpty())
            throw new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다.");
        return comments;
    }
}
