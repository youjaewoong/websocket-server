package com.websocket.server.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Redis에 메시지 발행이 될 떄까지 대기하였다가 메시지가 발행되면 해당 메시지를 읽어 처리하는 리스너
 * MessagerListener를 상속받아 onMessage 메서즈를 재작성한다. 아래에서는 Redis에 메시지가 발행되면
 * 해당 메시지를 ChatMessage로 변환하고 messaging Template을 이용하여 채팅방의 모든 websoket 클라이언트들에게
 * 메시지를 전달하도록 구현하였다. 
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

	public static List<String> messageList = new ArrayList<String>();

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			System.out.println("수신된 메시지 :: " + message);
			messageList.add(message.toString());
			System.out.println("Message received: " + message.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
//    private final ObjectMapper objectMapper;
//    private final RedisTemplate<?, ?> redisTemplate;
//    private final SimpMessageSendingOperations messagingTemplate;
//
//    /**
//     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
//     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            // redis에서 발행된 데이터를 받아 deserialize
//            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            // ChatMessage 객채로 맵핑
//            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
//            // Websocket 구독자에게 채팅 메시지 Send
//            messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
}