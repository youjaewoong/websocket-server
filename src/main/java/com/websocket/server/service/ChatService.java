package com.websocket.server.service;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.websocket.server.model.ChatRoom;
import com.websocket.server.redis.RedisKccSubscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final HashMap<String, RedisMessageListenerContainer> subscribers = new HashMap<>();
	private final RedisMessageListenerContainer redisMessageListener; //채팅방(topic)에 발행되는 메시지를 처리할 Listner
	private final RedisKccSubscriber redisSubscriber;
	
	
	public void redisAddStt(String key,  String jsonData) {
		log.info(">>># redisAddStt");
		
		String keys = asURL("/sub/redis/gg", key);
		
		//방생성
		addListener(keys);
		
		//publish
		redisTemplate.convertAndSend(
				keys,
				jsonData);//publish
	}
	
	
	/**
	 * Listner 추가
	 */
	private void addListener(String key) {
		log.info("addListener : {}",key);
		if(subscribers.get(key) != null) {
			return;
		}
		// 채널 생성
		ChannelTopic channel = new ChannelTopic(key);
		redisMessageListener.addMessageListener(redisSubscriber, channel);
		subscribers.put(key, redisMessageListener);	//구독 list에 추가
	}
	
	
	/**
	 * Listner 삭제
	 */
	public void removeSubscriber(String key) {
		log.info(">>># removeSubscriber key :: {}", key);
		if (subscribers.get(key) != null) {
			//stt 데이터에서 cmd E가 오면 리스트에서 삭제 처리
			redisMessageListener.removeMessageListener((MessageListener) subscribers.get(key));
			subscribers.remove(key);
		}
	}
	
	
	/**
     * @description publish URL
     * @param keys 키 목록 (ex: "stt", "0100", "rec-key")
     * @return URL (ex: "/sub/redis/{ext}/{info}" )
     */
    private String asURL(String... keys) {
        return String.join("/", keys);
    }
	
    
    /**
     * @description redis key set
     * @param keys 키 목록 (ex: "stt", "0100", "rec-key")
     * @return 키 (ex: "stt:0100:rec-key" )
     */
    private String asKey(String... keys) {
        return String.join(":", keys);
    }
	
}