package com.websocket.server.controller.redis.kcc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pubsub")
public class RedisController {

    @Autowired
    private RedisService redisService;// redis 서비스
    
    @SuppressWarnings("unused")
    private static HashOperations<String, Object, Object> hashOperations;
	@SuppressWarnings("unused")
	private static RedisTemplate<String, Object> redisTemplate;
    
	@SuppressWarnings("static-access")
	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }
    
    
	@PostMapping("/createSubscriber/{ext}")
	public void createSubscriber(@PathVariable String ext, @RequestBody String jsonData) {
		redisService.createSubscriber(ext, jsonData);
	}
	
	@PostMapping("/removeSubscriber/{ext}")
	public void removeSubscriber(@PathVariable String ext) {
		redisService.removeSubscriber(ext);
	}
	
	
}
