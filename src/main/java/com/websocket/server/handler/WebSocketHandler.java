package com.websocket.server.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.server.model.ChatMessage;
import com.websocket.server.model.ChatRoom;
import com.websocket.server.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
	private final ChatService chatService;
	
	/**
	 * Client로부터 받은 메시지를 Console Log에 출력하고
	 * Client로 환영 메시지를 보내는 역할을 한다.
	 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Client로부터 받은 메시지 : {}", payload);
        
        // [[basic]]
        //TextMessage textMessage = new TextMessage("Client로 보내는 역할 : Welcome chatting sever~^^");
        //session.sendMessage(textMessage);
        
        //웹소켓 클라이언트로부터 채팅 메시지를 전달받아 체팅 메시지 객체로 변환
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        
        //전달받은 메시지에 담긴 채팅방 ID로 발송 대상 채팅방 정보를 조회함
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        
        //해당 채팅방에 입장해있는 모든 클라이언드들에게 타입에 따른 메시지 발송
        room.handleActions(session, chatMessage, chatService);
        
    }
}
