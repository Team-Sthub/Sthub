package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Message;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.MessageQueryService;
import com.ssd.sthub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final MessageQueryService messageQueryService;

    // 메세지 전송
    @GetMapping("/send")
    public ResponseEntity<SuccessResponse<Message>> sendMsg(@RequestHeader("memberId") Long senderId, @RequestBody MessageDTO.Request request) {
        return ResponseEntity.ok(SuccessResponse.create(messageService.sendMsg(senderId, request)));
    }

    // 메세지 목록 조회

    // 특정유저와의 상세 메시지 조회

}
