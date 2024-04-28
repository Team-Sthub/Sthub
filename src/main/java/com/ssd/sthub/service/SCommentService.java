package com.ssd.sthub.service;

import com.ssd.sthub.domain.SComment;
import com.ssd.sthub.repository.SCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SCommentService {
    private final SCommentRepository sCommentRepository;

    // 중고거래글 댓글 작성
    public SComment createComment(SComment sComment) {
        return sCommentRepository.save(sComment);
    }
}
