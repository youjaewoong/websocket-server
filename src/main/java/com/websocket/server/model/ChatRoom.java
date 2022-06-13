package com.websocket.server.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import com.websocket.server.service.ChatService;

import java.util.HashSet;
import java.util.Set;

/**
 * 채팅방에 입장한 클라이언트들의 정보를 가지고 있어야하므로 
 * WebSocketSession 정보 리스트를 맴버 필드로 갖는다.
 */
@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}