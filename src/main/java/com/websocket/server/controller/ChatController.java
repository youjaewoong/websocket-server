package com.websocket.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.ChatMessage;
import com.websocket.server.redis.RedisPublisher;
import com.websocket.server.repo.ChatRoomRepository;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

 private final RedisPublisher redisPublisher;
 private final ChatRoomRepository chatRoomRepository;

 /**
  * 클라이언트가 채팅방 입장시 채팅방(topic)에서대화가 가능하도록 리스너를 연동하는
  * enterChatRoom 메서드를 셋팅한다. 채팅방에 발행된 메시지는 서로 다른서버에 공유하기 위해
  * redis의 Tipic으로 발행하였다.
  * 
  * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
  */
 @MessageMapping("/chat/message")
 @PostMapping("/chat/message")
 @ApiOperation("메시지 보내기")
 public void message(ChatMessage message) {
     if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
    	 chatRoomRepository.enterChatRoom(message.getRoomId());
         message.setMessage(message.getSender() + "님이 입장하셨습니다.");
     }
     // Websocket에 발행된 메시지를 redis로 발행한다(publish)
     redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
 }
}