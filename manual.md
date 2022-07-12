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

{"agent_status":[{"categories":[{"level":"L","name":"일반상환대출"},{"level":"M","name":"연체관리"},{"level":"S","name":"가상계좌요청"}]},{"categories":[{"level":"L","name":"재단관련"},{"level":"M","name":"기타"},{"level":"S","name":"기타"}]},{"categories":[{"level":"L","name":"홈페이지"},{"level":"M","name":"기타"},{"level":"S","name":"기타"}]},{"categories":[{"level":"L","name":"취업후 상환 학자금대출"},{"level":"M","name":"자발적상환"},{"level":"S","name":"가상계좌요청"}]},{"categories":[{"level":"L","name":"국가장학금유형I"},{"level":"M","name":"신청"},{"level":"S","name":"신청방법"}]}]}
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

publish /sub/redis/stt/1111 '{"ext":2810015,"cmd":"E","recKey":"987654321","dir":"T",  "index":"0","stt":"6988|7000|안녕하세요. KB국민은행 상담사 홍길동입니다.|R",  "dateTime":"2021-04-05 14:19:22.111","sttOnly":"안녕하세요. KB국민은행 상담사 홍길동입니다.","startTime":"111","endTime":"7000","who":"C","info":"stt" }'
```