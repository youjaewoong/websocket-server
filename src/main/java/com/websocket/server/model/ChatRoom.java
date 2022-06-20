package com.websocket.server.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Serializable {
	/**
	 * Redis에 저장되는 객체들은 Serialize가능해야 하므로 Serializable을 참조하도록
	 * 선언하고 serialVersionUID를 셋팅해준다.
	 */
	private static final long serialVersionUID = 7172418404911709986L;
	private String roomId;
    private String name;
	
    public static ChatRoom create(String name) {
    	ChatRoom chatRoom = new ChatRoom();
    	chatRoom.roomId = UUID.randomUUID().toString();
    	chatRoom.name = name;
    	return chatRoom;
	}
}