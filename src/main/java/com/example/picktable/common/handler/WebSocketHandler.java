package com.example.picktable.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established with session ID: {}", session.getId());

        Map<String, List<String>> headers = session.getHandshakeHeaders();

        String memberId = getHeader(headers, "memberId");
        String nickname = getHeader(headers, "nickname");

        if (memberId == null || nickname == null) {
            log.error("Invalid WebSocket connection: missing user information");
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        WebSocketSession existingSession = sessionMap.put(memberId, session);
        if (existingSession != null && existingSession.isOpen()) {
            existingSession.close(CloseStatus.NORMAL);
            log.info("Closed previous session for user {} with session ID: {}", memberId, existingSession.getId());
        }
        sessionMap.put(memberId, session);
        log.info("User {} connected with session ID: {}", memberId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Received WebSocket message: {}", message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket transport error: {}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("WebSocket connection closed with session ID: {}. Close status: {}", session.getId(), closeStatus);
        sessionMap.values().remove(session);
    }

    private String getHeader(Map<String, List<String>> headers, String key) {
        List<String> values = headers.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }
}
