package com.websocket.server.controller.redis.kcc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.websocket.server.redis.RedisKccSubscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	//@Autowired
	//private SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
	
	//채팅방(topic)에 발행되는 메시지를 처리할 Listner
	@Autowired
	private RedisMessageListenerContainer redisMessageListener;
	
	@Autowired
	private RedisKccSubscriber redisKccSubscriber;
	
	private Map<String, ChannelTopic> subscribers = new HashMap<>();

	public void createSubscriber(String ext, String jsonData) {
		log.info(">>># createSubscriber ext :: {}", ext);
		
		// 채널 생성
		String key = asURL("/sub/redis", ext);
		addListener(key);
		//publish
		redisTemplate.convertAndSend(
				key,
				jsonData);//publish
	}
		
		
	/**
	* Listner 삭제
	*/
	public void removeSubscriber(String ext) {
		log.info(">>># removeSubscriber ext :: {}", ext);
		String key = asURL("/sub/redis", ext);
		log.info("## {}" ,findAllListener().toString());
		if (subscribers.get(key) != null) {
			ChannelTopic channel = subscribers.get(key);
			subscribers.remove(key);
			redisMessageListener.removeMessageListener(redisKccSubscriber, channel);
			System.out.println(">>> : "+ findAllListener().toString());
		}
	}
	
	/**
	* Listner 전체조회
	*/
	public Set<String> findAllListener() {
		return subscribers.keySet();
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
		redisMessageListener.addMessageListener(redisKccSubscriber, channel);
		subscribers.put(key, channel);	//구독 list에 추가
	}
	
	
	
	/**
     * @description publish URL
     * @param keys 키 목록 (ex: "stt", "0100", "rec-key")
     * @return URL (ex: "/sub/redis/{ext}/{info}" )
     */
    private String asURL(String... keys) {
        return String.join("/", keys);
    }
}
