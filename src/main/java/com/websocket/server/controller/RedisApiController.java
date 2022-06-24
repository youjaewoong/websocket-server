package com.websocket.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("redis")
public class RedisApiController {
	
	private RedisTemplate<String, String> redisTemplate;
	private HashOperations<String, Object, Object> hashOperations;
	
	@Autowired
	RedisApiController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	/**
	 * key, value
	 * @return
	 */
	@PostMapping("/key-value")
	public ResponseEntity<?> createKeyValue() {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		vop.set("yellow", "banana");
		vop.set("red", "apple");
		vop.set("green", "watermelon");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	
	@GetMapping("/key-value/{key}")
	public ResponseEntity<?> getKeyValue(@PathVariable String key) {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		String value = vop.get(key);
		return new ResponseEntity<>(value, HttpStatus.OK);
	}
	
    
	/**
	 * hash key 생성
	 * @return
	 */
    @PostMapping("/hash")
    public ResponseEntity<?> createHash(@RequestBody String input) throws UnsupportedEncodingException {
        this.hashOperations.put("test","room", URLDecoder.decode(input, "UTF-8"));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    
    @PostMapping("/hash/push")
    public ResponseEntity<?> pushHash(@RequestBody String input) throws UnsupportedEncodingException {
        
        String inputs = "";
        if (redisTemplate.hasKey("test")){
            Map<Object,Object> redisTestRoomComponent = this.hashOperations.entries("test");
            //기존에 send_str에 값이 존재하는지 체크
            if (redisTestRoomComponent.get("room") != null) {
                inputs = redisTestRoomComponent.get("room").toString();
                inputs += ",";
            }
        }
        inputs += URLDecoder.decode(input, "UTF-8");
        // redis key(test)의 room에 inputs 을 넣음
        this.hashOperations.put("test","room",inputs);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    

    @GetMapping("/hash/{keyName}")
    public String getHash(@PathVariable String keyName) {
        
    	Map<Object,Object> room = this.hashOperations.entries(keyName);
        if (room.get("room") != null) {
        	return room.get("room").toString();
        }
        return null;
    }

    
    /**
     * key 삭제
     */
    @DeleteMapping("/key/{keyName}")
    public boolean deleteKey(@PathVariable String keyName) {
    	//cli : del {keyName}
        if (redisTemplate.hasKey(keyName)) {
        	return redisTemplate.delete(keyName);
        }
        return false;
    }
}
