package com.ssd.sthub.service;

import com.ssd.sthub.domain.SComment;
import com.ssd.sthub.dto.secondhand.SCommentDTO;
import com.ssd.sthub.repository.SCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SCommentQueryService {
    private final SCommentRepository sCommentRepository;

    // 중고거래 게시글 댓글 전체 조회
    public List<SCommentDTO.Response> getComments(Long secondhandId) {
        List<SComment> comments = sCommentRepository.findAllBySecondhandId(secondhandId);

        if(comments == null || comments.isEmpty())
            throw new EntityNotFoundException("해당 중고거래 게시글을 찾을 수 없습니다.");

        return comments.stream()
                .map(c -> new SCommentDTO.Response(c))
                .collect(Collectors.toList());
    }
}
