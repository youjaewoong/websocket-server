package com.websocket.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.ChatRoom;
import com.websocket.server.model.CreateRoom;
import com.websocket.server.service.ChatService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestBody CreateRoom createRoom) {
        return chatService.createRoom(createRoom);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}