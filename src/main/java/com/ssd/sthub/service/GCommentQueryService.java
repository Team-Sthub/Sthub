package com.ssd.sthub.service;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.repository.GCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GCommentQueryService {
    private final GCommentRepository gCommentRepository;

    // 공동구매 댓글 리스트 조회
    public List<GComment> getGCommentList(GroupBuying groupBuying) {
        return gCommentRepository.findAllByGroupBuying(groupBuying);
    }
}
