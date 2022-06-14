package com.websocket.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final SimpMessageSendingOperations messagingTemplate;
    
	/**
	 * @MessageMapping을 통해 Websocket으로 들어오는 메시지 발행을 처리한다.
	 * /pub/chat/message로 발행 요청하면 Controller이 해당 메시지를 받아 처리한다.
	 * 메시지가 발행되면 /sub/chat/room/{roomId}로 메시지를 send 하는 것을 볼 수 있다.
	 * [클라이언트]에서는 해당 주소를 (/sub/chat/room/{roomId} 구독(subscribe) 하고 있다가 메시지가 전달되면 화면에 출력하면 된다.
	 * @param message
	 */
    @MessageMapping("/chat/message")
    @PostMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
        	message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}