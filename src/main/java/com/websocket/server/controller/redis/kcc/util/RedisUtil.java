package com.websocket.server.controller.redis.kcc.util;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisUtil {
	
	// TODO: 에러처리 추가 예정

	@Autowired
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
	
	/**
	 * redis에 key로 조회된 String값 return
	 */
	public static String getString(String key){
		log.info("key  {}", key);
		
		String result = (String) stringOperations.get(key);
//		log.info("getString : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 key로 조회된 List 전체 조회
	 */
	public static List<?> getListAll(String key) {
		List<?> result = listOperations.range(key, 0, -1);
//		log.info("getListAll : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 key로 조회된 List를 특정 범위 조회
	 */
	public static List<?> getList(String key, int scount, int ecount) {
		List<?> result = listOperations.range(key, scount, ecount);
//		log.info("getList : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 key로 조회된 Set 전체 조회
	 */
	public static Object getSetAll(String key) {
		Object result = setOperations.members(key);
//		log.info("getSetAll : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 key로 조회된  Hash의 특정 값 조회
	 */
	public static Object getHash(String key, String key2) {
		Object result = hashOperations.get(key, key2);
//		log.info("getHash : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 key로 조회된  Hash의 전체 조회
	 */
	public static Map<Object, Object> getHashAll(String key) {
		Map<Object, Object> result = hashOperations.entries(key);
//		log.info("getHashAll : {} " , result.toString());
		return result;
	}
    
}