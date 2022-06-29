package com.websocket.server.controller.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.ChatMessage;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pubsubTest")
public class PubSubController {
	
	private final RedisMessageListenerContainer container;//topic에 메시지 방행을 기다리는 listner
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    
    private final HashMap<String, MessageListener> subscribers = new HashMap<>(); //구독자 리스트
   
    
    @GetMapping(value = "/test/testCreateSubscriber")
    public void testCreateSubscriber(ChatMessage chatmessage, @RequestParam(value = "ext") String ext) {
        log.info(">>># testCreateSubscriber");
        log.info("ext :: {}", ext);
        // 채널 생성
        final ChannelTopic channel = new ChannelTopic(ext);
        //리스너
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                System.out.println("수신된 메시지 :: " + message);
                testSubscriber(channel, message);
            }
        };

        //구독한다는 정보를 레디스메시지리스너에 등록
        container.addMessageListener(messageListener, channel);
        //subscribers.put(ext, messageListener);	//구독 list에 추가
    }
    
    
    @GetMapping(value = "/test/testSubscriber")
    public void testSubscriber(ChannelTopic channel, Message message) {
        log.info(">>># testSubscriber");
        // 채널 생성
        System.out.println(">>" + channel.getTopic()+" :: " + message);

        template.convertAndSend("/sub/redis/"+channel.getTopic(), message.toString());
    }

}
