package com.websocket.server.controller.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.RedisCache;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("redis/strings")
@Slf4j
public class RedisStringsController {
	
	private ValueOperations<String, String> valueOperations;
	
	@Autowired
	RedisStringsController(RedisTemplate<String, String> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }
	
	
	@ApiOperation("String 타입 저장")
    @PostMapping("set")
    public ResponseEntity<?> set(@RequestBody List<RedisCache> redisCache) {
    	
        redisCache.forEach(v -> {
        	valueOperations.set(v.getId(), String.valueOf(v.getText()), 60, TimeUnit.SECONDS);
        });
        return new ResponseEntity<>(redisCache, HttpStatus.CREATED);
    }
	
	
	@ApiOperation("String 타입 조회")
    @PostMapping("gets")
    public ResponseEntity<?> get(@RequestBody List<String> keys) {
		
		List<String> values = valueOperations.multiGet(keys);
		log.info("values size {}", values.size());
		
        return new ResponseEntity<>(values, HttpStatus.OK);
    }
	
	
	@ApiOperation("String 타입 조회")
    @GetMapping(value = "{key}", produces = "application/text; charset=utf8")
    public ResponseEntity<?> string(@PathVariable String key) {
		String value = valueOperations.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
	}
        		

    
}