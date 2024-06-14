package com.ssd.sthub.service;

import com.querydsl.core.QueryResults;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Message;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.dto.message.MessageListDTO;
import com.ssd.sthub.repository.MemberRepository;
import com.ssd.sthub.repository.MessageRepository;
import com.ssd.sthub.repository.MessageRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final MessageRepositoryImpl messageRepositoryImpl;
    private final ModelMapper modelMapper;

    // 쪽지 전송
    public MessageDTO.Response sendMsg(Long senderId, MessageDTO.Request request) throws NullPointerException {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Member receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Message msg = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .build();

        return new MessageDTO.Response(messageRepository.save(msg));
    }

    // 모든 유저와의 쪽지 내역 불러오기
    public MessageListDTO getAllMessage(int pageNum, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        QueryResults<Message> messages = messageRepositoryImpl.findMessagesByAllMember(memberId, pageNum, 4);

        List<MessageDTO.Response> result = messages.getResults().stream()
                .map(MessageDTO.Response::new)
                .collect(Collectors.toList());

        return new MessageListDTO(result, messages.getTotal());
    }

    // 기존에 있던 메시지 가져오는 로직을 서비스로 이동
    public List<MessageDTO.Response> getMessages(Long senderId, Long receiverId, Long lastMessageId) {
        if (lastMessageId == 0) {
            return getPersonalMessage(senderId, receiverId);
        } else {
            return getNewMessages(senderId, receiverId, lastMessageId);
        }
    }

    // 특정 유저와의 쪽지 내역 불러오기
    public List<MessageDTO.Response> getPersonalMessage(Long memberId, Long otherId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        Member other = memberRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        List<Message> messages = messageRepositoryImpl.findMessagesByMember(member, other);

        return messages.stream()
                .map(MessageDTO.Response::new)
                .collect(Collectors.toList());
    }

    public List<MessageDTO.Response> getNewMessages(Long memberId, Long otherId, Long messageId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        Member other = memberRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("회원 조회에 실패했습니다."));
        List<Message> messages = messageRepositoryImpl.findNewMessages(member, other, messageId);
        return messages.stream()
                .map(MessageDTO.Response::new)
                .collect(Collectors.toList());
    }
}