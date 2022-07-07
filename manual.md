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
---
### branch 02_stome

#### stomp
pub/sub구조로 되어있고 메시지를 발송한다 집배원(pubisher) -> 우체통(topic) -> 구독자(subscriber)
- 채팅방 생성 : pub/sub 구현을 위한 Topic이 하나 생성된다.
- 채팅방 입장 : Topic 구독
- 채팅방에서 메시지를 보내고 받는다 : 해당 Topic으로 메시지를 발송하거나(pub) 메시지를 받는다(sub)

### redis
```
string -> GET <key>
hash -> HGETALL <key>
lists -> lrange <key> <start> <end>
sets -> smembers <key>
sorted sets -> ZRANGEBYSCORE <key> <min> <max>
stream -> xread count <count> streams <key> <ID>.
```



```
categorizer:18090140711179
{"agent_status":[{"categories":[{"name":"교직원","level":"L"},{"name":"서울","level":"M"},{"name":"취업 후/일반","level":"S"},{"name":"최하위1","level":"SS"}]},{"categories":[{"name":"생활비대출","level":"L"},{"name":"신청","level":"M"},{"name":"대출제도","level":"S"},{"name":"","level":"SS"}]}]}
```

```
topics:18090140711179
sentence

[
   {
      "letter":"1금융위는 지난 2월..",
      "rank":1,
      "value":1.4553
   },
   {
      "letter":"2금융위는 지난 2월..",
      "rank":2,
      "value":1.4553
   },
   {
      "letter":"3금융위는 지난 3월..",
      "rank":3,
      "value":1.3553
   }
]
```