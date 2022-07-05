package com.websocket.server.controller.redis.kcc.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {
	private static final long serialVersionUID = -7730370530417834762L;
	private String topic;
	private String ext;
	private String message;

}
