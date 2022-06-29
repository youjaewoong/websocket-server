package com.websocket.server.controller.redis;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.ChatMessage;
import com.websocket.server.redis.RedisSubscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/pubsub")
public class RedisController {

	/**
	* 채팅방(topic)에 발행되는 메시지를 처리할 Listner
	*/
	private final RedisMessageListenerContainer redisMessageListener;
	
	@Autowired
	private final RedisSubscriber redisSubscriber;// 구독 처리 서비스
	
    private final HashMap<String, MessageListener> subscribers = new HashMap<>();

	
	@PostMapping("/createSubscriber/{ext}")
    public void createSubscriber(@PathVariable String ext
    							, ChatMessage chatmessage ) {
        log.info(">>># createSubscriber ext :: {}", ext);
        
        // 채널 생성
        ChannelTopic channel = new ChannelTopic(ext);
        chatmessage.setRoomId(channel.getTopic());
        redisMessageListener.addMessageListener(redisSubscriber, channel);

        //subscribers.put(ext, messageListener);	//구독 list에 추가
    }
	
	
	/**
	* Listner 삭제
	*/
	@PostMapping(value = "/removeSubscriber")
    public void removeSubscriber(@RequestParam(value = "ext") String ext) {
        log.info(">>># removeSubscriber ext :: {}", ext);
        if (subscribers.get(ext) != null) {
        	redisMessageListener.removeMessageListener(subscribers.get(ext));
            subscribers.remove(ext);
        }
    }
	
}
