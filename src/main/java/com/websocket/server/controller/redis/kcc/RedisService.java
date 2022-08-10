package com.websocket.server.controller.redis.kcc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.websocket.server.redis.RedisSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private SimpMessagingTemplate template; // 특정 Broker로 메세지를 전달

	// 채팅방(topic)에 발행되는 메시지를 처리할 Listner
	@Autowired
	private RedisMessageListenerContainer redisMessageListener;

	@Autowired
	private RedisSubscriber redisSubscriber;

	private Map<String, ChannelTopic> subscribers = new HashMap<>();

	public void createSubscriber(String ext, String path, String jsonData) {

		// 채널 생성
		String key = asURL("/sub/redis", path, ext).replace("//", "/");
		ChannelTopic channel = new ChannelTopic(key);
		// 리스너
		MessageListener messageListener = new MessageListener() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				subscriber(channel, message);
			}
		};
		if (subscribers.get(key) == null) {
			redisMessageListener.addMessageListener(messageListener, channel);
			subscribers.put(key, channel); // 구독 list에 추가
		}

	}

	public void subscriber(ChannelTopic channel, Message message) {
		log.info(">> {} :: {}", channel.getTopic(), message.toString());
		template.convertAndSend(channel.getTopic(), message.toString());
	}

	/**
	 * Listner 삭제
	 */
	public void removeSubscriber(String ext, String path) {
		log.info(">>># removeSubscriber ext :: {}", ext);
		String key = asURL("/sub/redis", ext, path);
		log.info("## {}", findAllListener().toString());
		if (subscribers.get(key) != null) {
			ChannelTopic channel = subscribers.get(key);
			subscribers.remove(key);
			redisMessageListener.removeMessageListener(redisSubscriber, channel);
			System.out.println(">>> : " + findAllListener().toString());
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
		log.info("addListener : {}", key);
		if (subscribers.get(key) != null) {
			return;
		}
		// 채널 생성
		ChannelTopic channel = new ChannelTopic(key);
		redisMessageListener.addMessageListener(redisSubscriber, channel);
		subscribers.put(key, channel); // 구독 list에 추가
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
