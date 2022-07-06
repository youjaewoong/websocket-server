package com.websocket.server.controller.redis.kcc.call;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.websocket.server.config.ObjectUtil;
import com.websocket.server.redis.RedisSubscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
	
	//채팅방(topic)에 발행되는 메시지를 처리할 Listner
	private RedisMessageListenerContainer redisMessageListener;
	
	@Autowired
	private RedisSubscriber redisSubscriber;
	
	private HashMap<String, RedisMessageListenerContainer> subscribers = new HashMap<>();
	
	/**
     * @description redis에 call meta 정보 추가 및 publish
     * @param Object data
     * @return 
     */
	public void redisAddCallInfomation(Object data) {
		log.info(">>># redisAddCallInfomation");
		
		Map<String, Object> callInfoMap = ObjectUtil.ObjectToMap(data);
		callInfoMap.put("info", "meta");
		
		//Object data To String
        String jsonData = ObjectUtil.ObjectToJson(callInfoMap);
		
		//redis save(type : hash)
		ObjectUtil.ObjectToRedisHash(
					asKey("callInformation", callInfoMap.get("ext").toString(), callInfoMap.get("callId").toString()),
					jsonData);
		
		String key = asURL("/sub/redis", callInfoMap.get("ext").toString(), "callInfo");
		addListener(key);
		//publish
		redisTemplate.convertAndSend(
				key,
				jsonData);//publish
	}
	
	/**
     * @description redis에 고객 정보 추가 및 publish
     * @param Object data
     * @return 
     */
	public void redisAddClientInfomation(Object data) {
		log.info(">>># redisAddClientInfomation");
		
		Map<String, Object> clientInfoMap = ObjectUtil.ObjectToMap(data);
		clientInfoMap.put("info", "client");
		
		//Object data To String
        String jsonData = ObjectUtil.ObjectToJson(clientInfoMap);
		
		//redis save(type : hash)
		ObjectUtil.ObjectToRedisHash(
				asKey("clientInformation", clientInfoMap.get("ext").toString(), clientInfoMap.get("callId").toString()),
				jsonData);	
		
		//publish
		String key = asURL("/sub/redis", clientInfoMap.get("ext").toString(), "client");
		addListener(key);

		redisTemplate.convertAndSend(
				key,
				jsonData);//publish
	}
	
	/**
     * @description redis에 고객 정보 추가 및 publish
     * @param Object data
     * @return 
     */
	public void redisAddStt(Object data) {
		log.info(">>># redisAddStt");
		
		Map<String, Object> sttMap = ObjectUtil.ObjectToMap(data);
		sttMap.put("info", "stt");
		
		//Object data To String
        String jsonData = ObjectUtil.ObjectToJson(sttMap);
		
        //redis save(type : List)
		ObjectUtil.ObjectToRedisList(
				asKey("stt", sttMap.get("ext").toString(), sttMap.get("recKey").toString()),
				jsonData);//redis save
		
		//redis save(type : List)
		ObjectUtil.ObjectToRedisSet(
				asKey("sttSet", sttMap.get("ext").toString(), sttMap.get("recKey").toString()),
				jsonData);//redis save
		
		String key = asURL("/sub/redis", sttMap.get("ext").toString(), "stt");
		if(sttMap.get("cmd").toString() != null && sttMap.get("cmd").toString().equals("E")) {
			removeSubscriber(key);
		}else {
			addListener(key);
		}
		//publish
		redisTemplate.convertAndSend(
				key,
				jsonData);//publish
	}
	
	
	
	public Set<String> findAllListener() {
		return subscribers.keySet();
	}
	
	/**
	 * Listner 추가
	 */
	private void addListener(String key) {
		log.info("addListener : {}",key);
		if(subscribers.get(key) == null) {
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
