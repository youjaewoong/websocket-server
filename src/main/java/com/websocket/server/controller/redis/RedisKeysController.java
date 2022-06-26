package com.websocket.server.controller.redis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("redis/keys")
public class RedisKeysController {
	

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	
	@ApiOperation("key 등록")
	@PostMapping()
	public ResponseEntity<?> createKey() {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		vop.set("yellow", "banana");
		vop.set("red", "apple");
		vop.set("green", "watermelon");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@ApiOperation("key value 조회")
	@GetMapping("{key}")
	public ResponseEntity<?> getKeyValue(@PathVariable String key) {
		return new ResponseEntity<>(redisTemplate.opsForValue().get(key), HttpStatus.OK);
	}
	
	
	@ApiOperation("key 타입 조회")
	@GetMapping("types/{key}")
	public ResponseEntity<?> getKeyType(@PathVariable String key) {
		return new ResponseEntity<>(redisTemplate.type(key), HttpStatus.OK);
	}
	
	
	@ApiOperation("key 개수 반환")
	@PostMapping("counts/{key}")
	public ResponseEntity<?> getKeyCounts(@RequestBody List<String> keys) {
		Long counts = redisTemplate.countExistingKeys(keys);
		return new ResponseEntity<>(counts, HttpStatus.OK);
	}
	
	
	@ApiOperation("key가 존재하는지 확인")
	@GetMapping("has/{keyName}")
	public ResponseEntity<?> hasKey(@PathVariable String keyName) {
		return new ResponseEntity<>(redisTemplate.hasKey(keyName), HttpStatus.OK);
	}
	
	
	@ApiOperation("Key 만료 날짜 세팅")
    @GetMapping("expires/at-setup/{key}")
    public ResponseEntity<?> expireAtSetup(@PathVariable String key) {
    	return new ResponseEntity<>(
    			redisTemplate.expireAt(key,
    			Date.from(LocalDateTime.now()
    					.plusDays(1L)
    					.atZone(ZoneId.systemDefault()).toInstant())), HttpStatus.OK);
    }
	
	
	//Key 만료시간이 세팅 안되어있는경우 -1 반환
	@ApiOperation("Key 만료 시간 세팅")
    @GetMapping("expires/time-setup/{key}")
    public ResponseEntity<?> expireTimeSetup(@PathVariable String key) {
    	return new ResponseEntity<>(redisTemplate.expire(key, 60, TimeUnit.SECONDS), HttpStatus.OK);
    }
	
	
	@ApiOperation("Key 만료 시간 조회")
    @GetMapping("expires/{key}")
    public ResponseEntity<?> getExpire(@PathVariable String key) {
    	return new ResponseEntity<>(redisTemplate.getExpire(key), HttpStatus.OK);
    }
	
	
	@ApiOperation("Key 만료 시간 해제")
    @GetMapping("expires/persist/{key}")
    public ResponseEntity<?> persist(@PathVariable String key) {
    	return new ResponseEntity<>(redisTemplate.persist(key), HttpStatus.OK);
    }
	
	
	@ApiOperation("key 제거")
    @DeleteMapping("{key}")
    public ResponseEntity<?> deleteKey(@PathVariable String key) {
        return new ResponseEntity<>(redisTemplate.delete(key), HttpStatus.OK);
    }
	
	
	@ApiOperation("Key 건별 삭제")
    @DeleteMapping("all/{keyName}")
    public ResponseEntity<?> deleteAllKey(@RequestBody List<String> keys) {
        return new ResponseEntity<>(redisTemplate.delete(keys), HttpStatus.OK);
    }
	
	
	@ApiOperation("keys 전체 삭제")
	@DeleteMapping()
    public ResponseEntity<?> deleteAll() {
		redisTemplate.keys("*").stream().forEach(k-> {
			   redisTemplate.delete(k);
		});
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
