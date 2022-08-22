package com.websocket.server.controller.redis.kcc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeMessage implements Serializable {
	
	private static final long serialVersionUID = -7730370530417834762L;
	
	private String topic;

	private Message message;
	
	@Getter
	@Setter
	private static class Message {
		
		private String id;
		
		private String publisherId;
		
		private Category category;
		
		private AgentType agentType;
		
		private eventType eventType;
		
		private List<String> targetIds = new ArrayList<>();
		
		private String message;
	}
	
	//이벤트 발생 구분  ADD, DELETE, UPDATE, RENOTICE
	private enum eventType {
		ADD,DELETE,UPDATE,RENOTICE;
	}
	
	//발행자 구분
	private enum AgentType {
		AGENT,ADMIN;
	}
	
	//카테고리 구분 E: 긴급 N: 일반, W:워닝
	private enum Category {
		E,N,W;
	}
}
