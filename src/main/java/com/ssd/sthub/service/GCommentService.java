package com.ssd.sthub.service;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.repository.GCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GCommentService {

    private final GCommentRepository gCommentRepository;

    // 공동구매 댓글 작성
    public GComment createGComment(GComment gComment) throws NullPointerException {
        if (gComment == null) {
            throw new NullPointerException("작성된 댓글 내용이 없습니다.");
        }

        return gCommentRepository.save(gComment);
    }
}