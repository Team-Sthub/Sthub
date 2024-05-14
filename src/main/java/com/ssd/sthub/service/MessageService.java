package com.ssd.sthub.service;

import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Message;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    // 쪽지 전송
    public MessageDTO.Response sendMsg(Long senderId, MessageDTO.Request request) throws NullPointerException {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Member reciever = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Message msg = Message.builder()
                .sender(sender)
                .receiver(reciever)
                .content(request.getContent())
                .build();

        return new MessageDTO.Response(messageRepository.save(msg));
    }
}
