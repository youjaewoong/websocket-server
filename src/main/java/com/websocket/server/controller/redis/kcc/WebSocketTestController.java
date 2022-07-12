package com.websocket.server.controller.redis.kcc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class WebSocketTestController {

	private final SimpMessagingTemplate template; // 특정 Broker로 메세지를 전달
//   private HashOperations<String, Object, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplate;
	private ValueOperations<String, Object> stringOperations;

	/*
	 *
	 * /chat/test 로 소켓 연결
	 *
	 * /publish/message : 값 보낼때 /subscribe/message : 값 받을때
	 *
	 */
	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.stringOperations = redisTemplate.opsForValue();
	}

	// 단순히 레디스에만 값 저장하는 테스트 API
	@PostMapping("/sendString")
	public String sendTest(@RequestBody String input, @RequestParam String ext)
			throws UnsupportedEncodingException {
		log.info("RedisTestController.sendTest");
		log.info("input : " + input);

		log.info("ext : " + ext);

		String key = "testString";

		stringOperations.set(key, input); // testSting 이라는 키값으로 redis에 값 넣음

		template.convertAndSend("/sub/redis/stt/" + ext, input);
		return input;
	}

	@MessageMapping(value = "/redis/enter") // /publisher/message
	public void message() {
		log.info("SEND TO REDIS..");

		String returnValue = "";
		String key = "testString";

		if (stringOperations.get(key) != null) {
			log.info("key value : " + stringOperations.get(key));
			returnValue = stringOperations.get(key).toString();
			template.convertAndSend("/sub/redis/1111", returnValue);
		}

		template.convertAndSend("/sub/redis/1111", returnValue);
	}

	@MessageMapping(value = "/redis/enter2") // /publisher/message
	public void message2(Map<String, String> param) {

		String ext = param.get("ext");
		String msg = param.get("msg");

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>  87  WebSocketTestController");
		System.out.println(param);
		System.out.println(ext);
		System.out.println(msg);

		template.convertAndSend("/sub/redis/" + ext, msg);
	}
}
