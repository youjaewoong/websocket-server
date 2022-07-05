package com.websocket.server.controller.redis.kcc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.WebSocketTest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class WebSocketTestController {

    private HashOperations<String, Object, Object> hashOperations;
    private final SimpMessagingTemplate websocketSimpMessagingTemplate; //특정 Broker로 메세지를 전달

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    /*
    *
    * /chat/test 로 소켓 연결
    *
    * /publish/message : 값 보낼때
    * /subscribe/message : 값 받을때
    *
    * */

    @MessageMapping(value = "/message")    // /publisher/message
    public void message(WebSocketTest webSocketTestEntity){
        this.sendRedis(webSocketTestEntity.getValue());
        websocketSimpMessagingTemplate.convertAndSend("/subscribe/message",webSocketTestEntity.getValue());
    }

    private void sendRedis(String input){
        log.info("SEND TO REDIS.." + input);
        String inputs = "";
        if(this.hashOperations.get("REDIS_TEST_ROOM","send_str")!=null){
            inputs = (String) this.hashOperations.get("redis_test_room","send_str");
        }
        inputs += (","+input);
        this.hashOperations.put("REDIS_TEST_ROOM","send_str",inputs);
        log.info("current stack inputs : " + inputs);
    }

    private String getRedis(){
        log.info("getRedis()");
        Map<Object,Object> redisTestRoomComponent = this.hashOperations.entries("redis_test_room");
        if(redisTestRoomComponent.get("send_str")==null){
            return "";
        }else{
            return redisTestRoomComponent.get("send_str").toString();
        }
    }
}
