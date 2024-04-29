package com.ssd.sthub.service;

import com.ssd.sthub.domain.GComment;
import com.ssd.sthub.domain.GroupBuying;
import com.ssd.sthub.dto.gComment.GCommentResponseDto;
import com.ssd.sthub.repository.GCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GCommentQueryService {
    private final GCommentRepository gCommentRepository;

    // 공동구매 댓글 리스트 조회
    public GCommentResponseDto getGCommentList(GroupBuying groupBuying) {
        List<GComment> gComments = gCommentRepository.findAllByGroupBuying(groupBuying);
        List<GCommentResponseDto.GCommentDto> gCommentListDto = gComments.stream()
                .map(gComment -> new GCommentResponseDto.GCommentDto(gComment.getId(), gComment.getContent(), gComment.getMember().getNickname()))
                .collect(Collectors.toList());

        return new GCommentResponseDto(gCommentListDto);
    }
}
