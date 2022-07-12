package com.websocket.server.controller.redis.kcc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pubsub")
@Slf4j
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
	public void createSubscriber(@PathVariable String ext, @RequestParam(value="depth", required=false) String depth, @RequestBody String jsonData) {
		log.info("depth {}",depth);
		int cntComma=0;
		Matcher m = Pattern.compile(",").matcher(depth);
		while (m.find()){
			cntComma++;
		}
		if(cntComma>0){
			String[] depthArr = depth.split(",");
			for(String de : depthArr) {
				redisService.createSubscriber(ext, de, jsonData);
			}
		}else{
			redisService.createSubscriber(ext, depth, jsonData);
		}
	}
	
	@PostMapping("/removeSubscriber/{ext}")
	public void removeSubscriber(@PathVariable String ext, @RequestParam(value="depth", required=false) String depth) {
//		redisService.removeSubscriber(ext, path);
		log.info("depth {}",depth);
		if(!depth.equals(null) && depth.indexOf(',') >0) {
			String[] depthArr = depth.split(",");
			for(String de : depthArr) {
				redisService.removeSubscriber(ext, de);
			}
		}
	}
	
}
