package com.websocket.server.controller.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("redis/lists")
@Slf4j
public class RedisListsController {
	
	/**
	 * Lists structure command – 순서 있음. value 중복 허용
	 * redis command lpush, llen, lrange, lpop, rpop에 대한 내용입니다.
	 */
	private ListOperations<String, String> listOperations;
	private RedisTemplate<String, String> redisTemplate;
	
	
	@Autowired
	RedisListsController(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }
	
	
	@ApiOperation("List 타입 lpush, llen, lrange, lpop, rpop에 대한 내용")
    @PostMapping()
    public ResponseEntity<?> list(@RequestParam(required = true) String key, 
    							  @RequestBody List<String> values) {
    	
//		values.forEach(v -> {
//        	listOperations.leftPush(key, String.valueOf(v));
//        });
		
		for (int i = 0; i < 10; i++) {
			listOperations.leftPush(key, String.valueOf(i));
		}
        
        log.info("type :: {}", redisTemplate.type(key));
        log.info("{}", listOperations.range(key, 0, 10));
        log.info("rightPop :: {}", listOperations.rightPop(key));
        log.info("leftPop :: {}", listOperations.leftPop(key));
        
        Long size = listOperations.size(key);
        for (int i = 0; i < size; i++) {
        	log.info("leftPop :: {}", listOperations.rightPop(key));
		}
        
        log.info("size :: {}", listOperations.size(key));
        log.info("key delete :: {}", redisTemplate.delete(key));
    
        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }
	
    
}