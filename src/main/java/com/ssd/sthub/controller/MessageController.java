package com.ssd.sthub.controller;

import com.ssd.sthub.domain.Message;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.dto.message.MessageListDTO;
import com.ssd.sthub.response.SuccessResponse;
import com.ssd.sthub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final ChatController chatController; // ChatController를 주입받음

    // 메세지 전송
    @PostMapping("/send")
    public String sendMsg(@SessionAttribute(name = "memberId") Long senderId,
                          @RequestParam("receiverId") Long receiverId,
                          @ModelAttribute MessageDTO.Request request) {
        request.setReceiverId(receiverId);
        System.out.println("받는 사람의 아이디는 " + request.getReceiverId());
        //chatController.sendMessage(senderId, receiverId, request); // WebSocket을 통해 sendMessage() 메소드 호출
        System.out.println("메세지 전송 완료");
        messageService.sendMsg(senderId, request);
        return "redirect:/message/detail?receiverId=" + receiverId;
    }

    // 모든 유저와의 쪽지 내역 조회
    @GetMapping("/list")
    public ModelAndView getMsgList(@SessionAttribute(name = "memberId") Long senderId,
                                   @RequestParam(defaultValue = "0") int pageNum) throws BadRequestException {
        MessageListDTO messageListDTO = messageService.getAllMessage(pageNum, senderId);
        ModelAndView modelAndView = new ModelAndView("thyme/message/list");
        if (messageListDTO.getResult().isEmpty()) {
            modelAndView.addObject("noMessage", "아직 쪽지를 주고받은 상대가 없습니다.");
        }
        modelAndView.addObject("messageList", messageListDTO);
        modelAndView.addObject("currentUserId", senderId);
        return modelAndView;
    }

    // 특정 유저와의 쪽지 내역 조회
    @GetMapping("/detail")
    public ModelAndView getDetailMsgList(@SessionAttribute(name = "memberId") Long senderId, @RequestParam Long receiverId) {
        List<MessageDTO.Response> messageList = messageService.getPersonalMessage(senderId, receiverId);
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        ModelAndView modelAndView = new ModelAndView("thyme/message/detail");
        modelAndView.addObject("messageList", messageList);
        modelAndView.addObject("currentUserId", senderId);
        return modelAndView;
    }
}
