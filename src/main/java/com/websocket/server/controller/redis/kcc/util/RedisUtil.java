package com.websocket.server.controller.redis.kcc.util;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisUtil {

	
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
	 * redis에 저장된 값 return
	 */
	public static Object getString(String key) {
		Object result = stringOperations.get(key);
		log.info("getString : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 저장된 값 return
	 */
	public static List<?> getListAll(String key) {
		List<?> result = listOperations.range(key, 0, -1);
		log.info("getListAll : {} " , result.toString());
		return result;
	}
	
	public static List<?> getList(String key, int scount, int ecount) {
		List<?> result = listOperations.range(key, scount, ecount);
		log.info("getList : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 저장된 값 return
	 */
	public static Object getSetAll(String key) {
		Object result = setOperations.members(key);
		log.info("getSetAll : {} " , result.toString());
		return result;
	}
	
	/**
	 * redis에 저장된 값 return
	 */
	public static Object getHash(String key, String key2) {
		Object result = hashOperations.get(key, key2);
		log.info("getHash : {} " , result.toString());
		return result;
	}
    
}
