package com.websocket.server.controller.redis.kcc;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    
	@PostMapping("/createSubscriber/{ext}")
	public void createSubscriber(@PathVariable String ext, @RequestBody String jsonData) {
		redisService.createSubscriber(ext, jsonData);
	}
	
	@PostMapping("/removeSubscriber/{ext}")
	public void removeSubscriber(@PathVariable String ext) {
		redisService.removeSubscriber(ext);
	}
	
}
