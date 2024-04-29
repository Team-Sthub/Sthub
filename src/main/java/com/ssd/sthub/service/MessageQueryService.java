package com.ssd.sthub.service;

import com.ssd.sthub.domain.Message;
import com.ssd.sthub.repository.MessageRepositoriy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageQueryService {
    private final MessageRepositoriy messageRepositoriy;

    // 모든 유저와의 쪽지 내역 불러오기
    public Page<Message> getAllMessage(int pageNum, long memberId) {
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        return messageRepositoriy.findAllByMemberId(memberId, pageRequest);
    }

    // 특정 유저와의 쪽지 내역 불러오기
    public List<Message> getPersonalMessage(Long memberId, Long otherId) {
        return messageRepositoriy.findAllByMemberIdAndOtherId(memberId, otherId);
    }
}
