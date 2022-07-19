package com.websocket.server.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websocket.server.controller.redis.kcc.model.SttInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ObjectUtil {
	
	private static RedisTemplate<String, Object> redisTemplate;
	private static ValueOperations<String, Object> stringOperations;
	private static ListOperations<String, Object> listOperations;
	private static HashOperations<String, Object, Object> hashOperations;
	private static SetOperations<String, Object> setOperations;
	private static ZSetOperations<String, Object> zsetOperations;
	
	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringOperations = redisTemplate.opsForValue();
        this.listOperations = redisTemplate.opsForList();
        this.hashOperations = redisTemplate.opsForHash();
        this.setOperations = redisTemplate.opsForSet();
        this.zsetOperations = redisTemplate.opsForZSet();
    }

	public static <T> List<T> objToList(T obj){
		Gson gson = new Gson();
		String objJson = "";
		try{
			objJson = gson.toJson(obj);
			if(StringUtils.isNotEmpty(objJson)){
				if(objJson.charAt(0)=='['){
					return gson.fromJson(objJson,new TypeToken<List<T>>(){}.getType());
				}
			}
		}catch (Exception e){
			log.error("this is not list");
			log.error(e.toString());
			return null;
		}
		return null;
	}

  	public static String objectToJson(Object obj) {
  		ObjectMapper jsonMapper = new ObjectMapper();
  		try {
  			return jsonMapper.writeValueAsString(obj);
  		} catch (JsonProcessingException e) {
  			throw new RuntimeException(e);
  		}
	}
  	
  	public static Map<String, Object> objectToMap(Object obj){
  		try {
  			String jsonString = objectToJson(obj);
			ObjectMapper jsonMapper = new ObjectMapper();
			TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
			Map<String, Object> returnMap = jsonMapper.readValue(jsonString, typeRef);
			if(returnMap.get("stt")!=null){
				//2022.07.08 여기에 callinfo 조회하는거 해야하는데 현재 selectStt에는 callId가 없어서 조회를 할수가 없음;;
				//2022.07.15 selectStt에는 callId을 못준데요 
				{}
				if(returnMap.get("dir")!=null && StringUtils.isNotEmpty(returnMap.get("dir").toString())){
					SttInfo sttInfo = new SttInfo(returnMap.get("stt").toString(),null,returnMap.get("dir").toString());
					returnMap.put("sttOnly",sttInfo.getSttText().trim());
					returnMap.put("startTime",sttInfo.getStartTime());
					returnMap.put("endTime",sttInfo.getEndTime());
					returnMap.put("who",sttInfo.getWho());
				}
			}

			return returnMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
  	}
	  
  	public static String objectToRedisString(String key, String obj) {
  		log.info("## ObjectToRedisString");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		
  		String returnCode = "Y";
  		try {
  			stringOperations.set(key, obj); // redis set 명령어
  		} catch (NullPointerException e) {
  			log.error("null >> "+e.toString());
  			returnCode = "N";
		} catch (Exception e) {
			log.error("exception >> "+e.toString());
			returnCode = "N";
		}
  		return returnCode;
  	}
  	
  	public static String objectToRedisList(String key, String obj) {
  		log.info("## ObjectToRedisList");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		String returnCode = "Y";
  		
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			listOperations.rightPush(key, obj); // redis List 끝에 추가
		} catch (NullPointerException e) {
  			log.error("null >> "+e.toString());
  			returnCode = "N";
		} catch (Exception e) {
			log.error("exception >> "+e.toString());
			returnCode = "N";
		}
  		return returnCode;
	    
  	}
  	
  	public static String objectToRedisSet(String key, String obj) {
  		log.info("## ObjectToRedisSet");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		String returnCode = "Y";
  		
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			setOperations.add(key, obj); // redis List 끝에 추가
		} catch (NullPointerException e) {
  			log.error("null >> "+e.toString());
  			returnCode = "N";
		} catch (Exception e) {
			log.error("exception >> "+e.toString());
			returnCode = "N";
		}
  		return returnCode;
	    
  	}
  	
//  	public static void objectToRedisZSet(String key, String obj) {
//  		log.info("## ObjectToRedisZSet");
//  		log.info("key : {}", key );
//  		log.info("obj : {}", obj );
//  		try {
//  			zsetOperations.add(key, obj, score); // redis List 끝에 추가
//		} catch (NullPointerException e) {
//  			log.error("null >> "+e.toString());
//  			//throw new RuntimeException(e);
//		} catch (Exception e) {
//			log.error("exception >> "+e.toString());
//			//throw new RuntimeException(e);
//		}
//	    
//  	}
  	
  	public static String objectToRedisHash(String key, String obj) {
  		log.info("## ObjectToRedisHash");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		String returnCode = "Y";
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			hashOperations.putAll(key, pramsMap); // redis hash 추가
  		} catch (NullPointerException e) {
  			log.error("null >> "+e.toString());
  			returnCode = "N";
  		} catch (Exception e) {
  			log.error("exception >> "+e.toString());
  			returnCode = "N";
  		}
  		return returnCode;
  	}
  	
  	public static void objectToSend(String url, Object data) {
  		RestTemplate restTemplate = new RestTemplate();
  		
  		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(data, headers);
        
        restTemplate.postForObject(url, request, String.class);
  	}
}