package com.websocket.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.server.model.ChatRoom;
import com.websocket.server.model.CreateRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    //채팅방 조회
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    //채팅방 생성 roomId가 개별 key
    public ChatRoom createRoom(CreateRoom createRoom) {
        //String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(createRoom.getRoomId())
                .name(createRoom.getName())
                .build();
        chatRooms.put(createRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    //지정한 Websocket 세션에 메시지 발송
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
