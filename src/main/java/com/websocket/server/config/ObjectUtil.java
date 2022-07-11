package com.websocket.server.config;

import java.util.HashMap;
import java.util.Map;

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

  	public static String ObjectToJson(Object obj) {
  		ObjectMapper jsonMapper = new ObjectMapper();
  		try {
  			return jsonMapper.writeValueAsString(obj);
  		} catch (JsonProcessingException e) {
  			throw new RuntimeException(e);
  		}
	}
  	
  	public static Map<String, Object> ObjectToMap(Object obj){
  		try {
  			String jsonString = ObjectToJson(obj);
			ObjectMapper jsonMapper = new ObjectMapper();
			TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
			Map<String, Object> returnMap = jsonMapper.readValue(jsonString, typeRef);
			return returnMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
  	}
	  
  	public static void ObjectToRedisString(String key, String obj) {
  		log.info("## ObjectToRedisString");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		try {
  			stringOperations.set(key, obj); // redis set 명령어
  		} catch (NullPointerException e) {
  			log.error("null >> "+e.getMessage());
  			//throw new RuntimeException(e);
		} catch (Exception e) {
			log.error("exception >> "+e.getMessage());
			//throw new RuntimeException(e);
		}
  	}
  	
  	public static void ObjectToRedisList(String key, String obj) {
  		log.info("## ObjectToRedisList");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			listOperations.rightPush(key, obj); // redis List 끝에 추가
		} catch (NullPointerException e) {
  			log.error("null >> "+e.getMessage());
  			//throw new RuntimeException(e);
		} catch (Exception e) {
			log.error("exception >> "+e.getMessage());
			//throw new RuntimeException(e);
		}
	    
  	}
  	
  	public static void ObjectToRedisSet(String key, String obj) {
  		log.info("## ObjectToRedisSet");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			setOperations.add(key, obj); // redis List 끝에 추가
		} catch (NullPointerException e) {
  			log.error("null >> "+e.getMessage());
  			//throw new RuntimeException(e);
		} catch (Exception e) {
			log.error("exception >> "+e.getMessage());
			//throw new RuntimeException(e);
		}
	    
  	}
  	
//  	public static void ObjectToRedisZSet(String key, String obj) {
//  		log.info("## ObjectToRedisZSet");
//  		log.info("key : {}", key );
//  		log.info("obj : {}", obj );
//  		try {
//  			zsetOperations.add(key, obj, score); // redis List 끝에 추가
//		} catch (NullPointerException e) {
//  			log.error("null >> "+e.getMessage());
//  			//throw new RuntimeException(e);
//		} catch (Exception e) {
//			log.error("exception >> "+e.getMessage());
//			//throw new RuntimeException(e);
//		}
//	    
//  	}
  	
  	public static void ObjectToRedisHash(String key, String obj) {
  		log.info("## ObjectToRedisHash");
  		log.info("key : {}", key );
  		log.info("obj : {}", obj );
  		Map<String, Object> pramsMap = new HashMap<>();
  		try {
  			pramsMap = new ObjectMapper().readValue(obj, Map.class);
  			//pramsMap = ObjectToMap(obj);
  			hashOperations.putAll(key, pramsMap); // redis hash 추가
  		} catch (NullPointerException e) {
  			log.error("null >> "+e.getMessage());
  			//throw new RuntimeException(e);
  		} catch (Exception e) {
  			log.error("exception >> "+e.getMessage());
  			//throw new RuntimeException(e);
  		}
  		
  	}
  	
  	public static void ObjectToSend(String url, Object data) {
  		RestTemplate restTemplate = new RestTemplate();
  		
  		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(data, headers);
        
        restTemplate.postForObject(url, request, String.class);
  	}
}