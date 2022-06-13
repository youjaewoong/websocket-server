package com.websocket.server.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoom {
	private String roomId;
	private String name;
	private List<?> sessions;
}