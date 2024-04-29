package com.ssd.sthub.service;

import com.ssd.sthub.domain.Message;
import com.ssd.sthub.repository.MessageRepositoriy;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepositoriy messageRepositoriy;

    // 쪽지 전송
    public Message sendMsg(Message message) throws NullPointerException{
        if (message== null) {
            throw new NullPointerException("내용을 작성해주세요.");
        }
        return messageRepositoriy.save(message);
    }
}
