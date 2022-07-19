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
import com.websocket.server.controller.redis.kcc.GlobalVariables;
import com.websocket.server.controller.redis.kcc.util.RedisUtil;
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
		
		Map<String, Object> callInfoMap = ObjectUtil.objectToMap(data);
		callInfoMap.put("info", "meta");
		callInfoMap.put("companyCode", callInfoMap.get("ext").toString().substring(0, 2)); // 계열사 구분코드
		
		//Object data To String
        String jsonData = ObjectUtil.objectToJson(callInfoMap);
		
		//redis save(type : hash)
        String redisKey = "";
        if(!callInfoMap.containsKey("callId") || callInfoMap.get("callId").equals(null)) {
        	redisKey = RedisUtil.asKey("callInformation", callInfoMap.get("ext").toString(), "callId-null");
        } else {
        	redisKey = RedisUtil.asKey("callInformation", callInfoMap.get("ext").toString(), callInfoMap.get("callId").toString());
        }
        
		String returnCode = ObjectUtil.objectToRedisHash(
							redisKey,
							jsonData);
		if(returnCode.equals("N")) {
			log.info("CallInfomation objectToRedisHash Error");
		}

//		SttMeta sttMeta = new Gson().fromJson(jsonData, SttMeta.class);
//		try{
//			sttMetaService.insert(sttMeta);
//		}catch (Exception e){
//			sttMetaService.update(sttMeta);
//		}

		String key = RedisUtil.asURL("/sub/redis/callInfo", callInfoMap.get("ext").toString());
		addListener(key);
		//publish
		redisTemplate.convertAndSend(
				key,
				jsonData);

		if(callInfoMap.get("apId")!=null&&callInfoMap.get("apId").toString().length()>0){
			if(callInfoMap.get("callId")!=null&&callInfoMap.get("callId").toString().length()>0){
				GlobalVariables.CALL_ID_AND_AP_ID_MAP.put(callInfoMap.get("callId").toString(),callInfoMap.get("apId").toString());
			}
		}
	}
	
	/**
     * @description redis에 고객 정보 추가 및 publish
     * @param Object data
     * @return 
     */
	public void redisAddClientInfomation(Object data) {
		log.info(">>># redisAddClientInfomation");
		
		Map<String, Object> clientInfoMap = ObjectUtil.objectToMap(data);
		clientInfoMap.put("info", "client");
		
		//Object data To String
        String jsonData = ObjectUtil.objectToJson(clientInfoMap);
		
		//redis save(type : hash)
		ObjectUtil.objectToRedisHash(
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
		
		Map<String, Object> sttMap = ObjectUtil.objectToMap(data);
		sttMap.put("info", "stt");
		sttMap.put("companyCode", sttMap.get("ext").toString().substring(0, 2)); // 계열사 구분코드

        //redis save(type : List)
        String redisKey = "";
        if(sttMap.get("cmd").toString() != null && sttMap.get("cmd").toString().equals("B")) {
        	String reckey = sttMap.get("recKey").toString();
        	if(reckey.indexOf(".") > 0) {
        		redisKey = RedisUtil.asKey("stt", sttMap.get("ext").toString(), reckey.substring(0, reckey.length()-4));
        	}
        }
        else {
        	redisKey = RedisUtil.asKey("stt", sttMap.get("ext").toString(), sttMap.get("recKey").toString());
        }
		
		String key = RedisUtil.asURL("/sub/redis/stt", sttMap.get("ext").toString());
		String jsonData = ObjectUtil.objectToJson(sttMap);
		log.debug("=============================stt!!!!===============================");
		log.debug(jsonData);

		addListener(key);
		//publish
		log.info("convertAndSend {}",key);
		String returnCode = ObjectUtil.objectToRedisHash(
										key,
										subscribers.toString());
				
		if(returnCode.equals("N")) {
			log.info("redisAddStt objectToRedisHash Error");
		}

		//memory DB(KEY)에 저장
		String returnCode2 = ObjectUtil.objectToRedisList(
										redisKey,
										jsonData);//redis save
		if(returnCode2.equals("N")) {
			log.info("redisAddStt objectToRedisList Error");
		}

		//FOR SAVE CALLID_RECKEY
		if(sttMap.get("cmd")!=null&&sttMap.get("cmd").toString().equalsIgnoreCase("I")){
			if(sttMap.get("reckey")!=null&&sttMap.get("callId")!=null){
				if(sttMap.get("reckey").toString().length()>0){
					GlobalVariables.REC_KEY_AND_CALL_ID_MAP.put(
							sttMap.get("reckey").toString(),
							sttMap.get("callId").toString()
					);
				}
			}
		}

		//publish
//		if(sttMap.get("cmd").toString() != null && sttMap.get("cmd").toString().equals("E")) {
		if(CallCheckService.isEndCall(key,sttMap)){
			log.debug("!!!IS END CALL!!!");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.error(e.toString());
			}

			jsonData=ObjectUtil.objectToJson(sttMap);
			redisTemplate.convertAndSend(
					key,
					jsonData);
			//
			sttMap.put("cmd","E");
			sttMap.put("stt",null);
			jsonData=ObjectUtil.objectToJson(sttMap);
			redisTemplate.convertAndSend(
					key,
					jsonData);

			removeSubscriber(key);

		}else{
			if(sttMap.get("cmd")!=null&&sttMap.get("cmd").toString().equalsIgnoreCase("E")){
				return;
			}
			redisTemplate.convertAndSend(
					key,
					jsonData);
		}

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
