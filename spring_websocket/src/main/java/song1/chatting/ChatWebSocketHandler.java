package song1.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import song1.chatting.domain.ChatData;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private Set<WebSocketSession> sessions = new HashSet<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatData chatData = objectMapper.readValue(payload, ChatData.class);
        log.info("수신: {}", chatData);

        broadcast(payload); // 모든 세션에 메시지 브로드캐스트
    }

    private void broadcast(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            session.sendMessage(textMessage);
        }
    }
}
