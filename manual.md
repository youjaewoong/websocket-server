### port
http://localhost:8089

### swagger
http://localhost:8089/swagger-ui/index.html

### 채팅방구조

- POST /chat 채팅방 생성
- 채팅방 입장 chome app websocket test client 에서 실행
`ws://localhost:8089/ws/chat`
```
{
  "type":"ENTER",
  "roomId":"gos40",
  "sender":"happydaddy",
  "message":""
}
```

```
// 메시지 발송
{
  "type":"TALK",
  "roomId":"gos40",
  "sender":"happydaddy",
  "message":"안녕하세요"
}
// 메시지 수신
{
   "type":"TALK",
   "roomId":"gos40",
   "sender":"happydaddy",
   "message":"안녕하세요"
}
```