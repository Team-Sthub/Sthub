package com.ssd.sthub.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, String> sessionMemberMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<WebSocketSession> threadLocalSession = new ThreadLocal<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결이 성립되면 호출되는 메서드
        // 여기서 세션에서 사용자 정보를 추출할 수 있습니다.
        String memberId = (String) session.getAttributes().get("memberId");
        if (memberId != null) {
            sessionMemberMap.put(session.getId(), memberId);
            threadLocalSession.set(session); // 현재 스레드의 WebSocket 세션을 저장합니다.
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지를 수신하면 호출되는 메서드
        // 여기서 메시지 처리를 수행합니다.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결이 종료되면 호출되는 메서드
        sessionMemberMap.remove(session.getId());
        threadLocalSession.remove();
    }

    public static String getMemberIdFromSession() {
        // 현재 스레드의 WebSocketSession을 가져옵니다.
        WebSocketSession session = threadLocalSession.get();
        if (session != null) {
            return sessionMemberMap.get(session.getId());
        }
        return null;
    }
}
