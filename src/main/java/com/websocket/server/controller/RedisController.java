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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RedisController {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private HashOperations<String, Object, Object> hashOperations;
	
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }
    

	@PostMapping("/redis/test")
	public ResponseEntity<?> addRedisKey() {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		vop.set("yellow", "banana");
		vop.set("red", "apple");
		vop.set("green", "watermelon");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/redis/test/{key}")
	public ResponseEntity<?> getRedisKey(@PathVariable String key) {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		String value = vop.get(key);
		return new ResponseEntity<>(value, HttpStatus.OK);
	}
	
    /*
    테스트 확인법
    - POSTMAN으로 확인할 수 있다.
    - redis key(방)의 이름을 redis_test_room 로 하드코딩하여 테스트 코드를 작성함
    - 모두 POST 방식

    1. sendTest, updateTest
    - input : str

    2. getTest, delTest
    - input : "redis_test_room"
    *
    * */
    
    //단순히 레디스에만 값 저장하는 테스트 API
    @PostMapping("/send")
    public String sendTest(@RequestBody String input) throws UnsupportedEncodingException {
        input = URLDecoder.decode(input, "UTF-8");
        log.info("RedisTestController.sendTest");
        log.info("input : " + input);

        // redis key(redis_test_room)의 send_str에 input을 넣음
        this.hashOperations.put("redis_test_room","send_str",input);
        return input;
    }

    @PostMapping("/update")
    public String updateTest(@RequestBody String input) throws UnsupportedEncodingException {
        input = URLDecoder.decode(input, "UTF-8");
        log.info("RedisTestController.sendTest");
        log.info("input : " + input);
        String inputs = "";

        //기존에 redis_test_room 가 존재하는지 체크
        if(redisTemplate.hasKey("redis_test_room")){
            Map<Object,Object> redisTestRoomComponent = this.hashOperations.entries("redis_test_room");
            //기존에 send_str에 값이 존재하는지 체크
            if(redisTestRoomComponent.get("send_str")!=null){
                inputs = redisTestRoomComponent.get("send_str").toString();
                inputs += ",";
            }
        }

        inputs += input;

        // redis key(redis_test_room)의 send_str에 inputs 을 넣음
        this.hashOperations.put("redis_test_room","send_str",inputs);
        log.info("current stack inputs : " + inputs);

        return inputs;
    }

    @PostMapping("/get")
    public String getTest(@RequestBody String keyName) throws UnsupportedEncodingException {
        keyName = URLDecoder.decode(keyName, "UTF-8");
        log.info("RedisTestController.getTest()");
        Map<Object,Object> redisTestRoomComponent = this.hashOperations.entries(keyName);
        String value = "";

        //redis key(방) 안에 send_str이라는 attr이 존재하면 그 값을 리턴
        if(redisTestRoomComponent.get("send_str")!=null){
            value = redisTestRoomComponent.get("send_str").toString();
            log.info(value);

        }
        return value;
    }

    @PostMapping("/del")
    public int delTest(@RequestBody String keyName) throws UnsupportedEncodingException {
        keyName = URLDecoder.decode(keyName, "UTF-8");
        // redis key(방) 삭제 api
        // cli cmd: del {keyName}
        if(Boolean.TRUE.equals(redisTemplate.hasKey(keyName))){
            redisTemplate.delete(keyName);
            return 1;
        }else{
            return 0;
        }
    }
}
