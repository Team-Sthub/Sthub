package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Message;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    // 메세지 전송
    @GetMapping("/send")
    public ResponseEntity<SuccessResponse<MessageDTO.Response>> sendMsg(@RequestHeader("memberId") Long senderId, @RequestBody MessageDTO.Request request) {
        return ResponseEntity.ok(SuccessResponse.create(messageService.sendMsg(senderId, request)));
    }

    // 메세지 목록 조회
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<Page<Message>>> getMsgList(@RequestHeader("memberId") Long senderId, @RequestParam int pageNum) throws BadRequestException {
        return ResponseEntity.ok(SuccessResponse.create(messageService.getAllMessage(pageNum, senderId)));
    }

    // 특정유저와의 상세 메시지 조회
    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<List<MessageDTO.Response>>> getDetailMsgList(@RequestHeader("memberId") Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(SuccessResponse.create(messageService.getPersonalMessage(senderId, receiverId)));
    }
}
