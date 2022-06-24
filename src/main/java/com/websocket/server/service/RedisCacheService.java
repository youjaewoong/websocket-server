package com.websocket.server.service;

import org.springframework.stereotype.Service;

import com.websocket.server.model.RedisCache;

@Service
public class RedisCacheService {

    public RedisCache getRedisCash(String id){
    	RedisCache redisCash = new RedisCache();
    	redisCash.setId(id);
    	redisCash.setText( id + "님, 안녕하세요~!");
        System.out.println("[id:" + id + "] Service 에서 연산을 수행합니다");
        return redisCash;
    }
}