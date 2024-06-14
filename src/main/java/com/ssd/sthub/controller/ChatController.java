package com.ssd.sthub.controller;

import com.ssd.sthub.config.MyWebSocketHandler;
import com.ssd.sthub.dto.message.MessageDTO;
import com.ssd.sthub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageDTO.Request request, SimpMessageHeaderAccessor accessor) {
        try {
            // Log the request for debugging
            System.out.println("Received request: " + request);
            System.out.println("Receiver ID: " + request.getReceiverId());

            // STOMP 헤더에서 memberId 추출
            Long senderId = Long.valueOf(accessor.getFirstNativeHeader("memberId"));
            System.out.println("Sender ID: " + senderId);

            // Handle the message logic
            MessageDTO.Response response = messageService.sendMsg(senderId, request);

            // 대화 ID 생성 (예: senderId와 receiverId를 조합하여 생성)
            String conversationId = createConversationId(senderId, request.getReceiverId());

            // Send response to the conversation
            messagingTemplate.convertAndSend("/sub/messages/" + conversationId, response);
        } catch (Exception e) {
            System.err.println("Error while sending message: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    @MessageMapping("/requestMessages")
    public void requestMessages(@Payload Map<String, String> queryParams) {
        System.out.println("메세지 요청 컨트롤러 진입");
        try {
            Long senderId = Long.parseLong(queryParams.get("senderId"));
            Long receiverId = Long.parseLong(queryParams.get("receiverId"));
            Long lastMessageId = queryParams.containsKey("lastMessageId") ? Long.parseLong(queryParams.get("lastMessageId")) : 0L;
            System.out.println("uri에서 추출한 senderId " + senderId + " receiverId " + receiverId + " lastMessageId " + lastMessageId);

            List<MessageDTO.Response> messages;
            if (lastMessageId == 0) {
                messages = messageService.getPersonalMessage(senderId, receiverId);
            } else {
                messages = messageService.getNewMessages(senderId, receiverId, lastMessageId);
            }

            System.out.println("메세지 가져오는 건 성공");

            // 대화 ID 생성
            String conversationId = createConversationId(senderId, receiverId);

            // Send messages to the conversation
            messagingTemplate.convertAndSend("/sub/messages/" + conversationId, messages);
        } catch (Exception e) {
            System.err.println("Error while requesting messages: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    private String createConversationId(Long senderId, Long receiverId) {
        // 대화 ID 생성 로직 (예: 두 ID를 조합하여 고유한 문자열 생성)
        return senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;
    }
}
