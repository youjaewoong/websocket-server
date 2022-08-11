package com.websocket.server.controller.redis.kcc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
	private ValueOperations<String, Object> stringOperations;


	// 단순히 레디스에만 값 저장하는 테스트 API
	@PostMapping("/sendString")
	public void sendTest(@RequestBody String input, @RequestParam String key, @RequestParam(required = false) String ext)
			throws UnsupportedEncodingException {
		if (ext == null) {
			template.convertAndSend("/sub/redis/" + key, input);
		} else {
			template.convertAndSend("/sub/redis/" + key + "/"+ ext, input);
		}
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
