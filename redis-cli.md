
### redis
```
string -> GET <key>
hash -> HGETALL <key>
lists -> lrange <key> <start> <end>
sets -> smembers <key>
sorted sets -> ZRANGEBYSCORE <key> <min> <max>
stream -> xread count <count> streams <key> <ID>.
```

###기본
```
저장(key value)
set str "value1"

가져오다
get str

key 조회
keys *

key 타입확인
type str

목록 끝에 넣는다
rpush list 1
rpush list 2

keys list

키에 저장된 목록의 길이 없으면 0 목록이 아니면 error
llen list

저장된 키의 값을 개수를 인덱스를 지정하여 가져올 수있음. 음수의 경우일 경우 마지막값을 의미
-1은 마지막값, -2 마지막에서 2번째값, -3 마지막에서 3번쨰값

lrange list 0 -1
1) "1"
2) "2"
3) "3"

lrange list 1 -1
1) "2"
2) "3"
lrange list 0 -1
```

```
hashlist format
topics:{rec-key}:word
topics:{rec-key}:sentence
categorizer:{rec-key}

HGET topics:18090140711179:word
HGET topics 18090140711179 word

hget topics:18090140711179 sentence
```
---
###실습

```
set calllInfo:1000:20220623155200_00001 '{  "callId": "20220623155200_00001",  "apId": "상담키",  "empId": "agent001",  "custId": "client001",  "callStartTime": "2022-06-23 15:52:00",  "inputTel": "010-1234-5678",  "ext": 1000,  "ioGbn": "I" }'

127.0.0.1:6379> keys *
1) "str"
2) "list"
3) "calllInfo:1000:20220623155200_00001"

get calllInfo:1000:20220623155200_00001
"{  \"callId\": \"20220623155200_00001\",  \"apId\": \"\xec\x83\x81\xeb\x8b\xb4\xed\x82\xa4\",  \"empId\": \"agent001\",  \"custId\": \"client001\",  \"callStartTime\": \"2022-06-23 15:52:00\",  \"inputTel\": \"010-1234-5678\",  \"ext\": 1000,  \"ioGbn\": \"I\" }"
127.0.0.1:6379> type calllInfo:1000:20220623155200_00001
string

rpush stt:1000:20220623155200_00001 '{"callId":"20180901185935_58066","ext":"1101", ...}'
rpush stt:1000:20220623155200_00001 '{"callId":"20180901185935_58066","ext":"1101", ...}'
rpush stt:1000:20220623155200_00001 '{"callId":"20180901185935_58066","ext":"1101", ...}'
rpush stt:1000:20220623155200_00001 '{"callId":"20180901185935_58066","ext":"1101", ...}'
rpush stt:1000:20220623155200_00001 '{"callId":"20180901185935_58066","ext":"1101", ...}'

keys *
1) "stt:1000:20220623155200_00001"
2) "str"
3) "list"
4) "calllInfo:1000:20220623155200_00001"

type stt:1000:20220623155200_00001
list

lrange stt:1000:20220623155200_00001 0 -1
1) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
2) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
3) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
4) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
5) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
6) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
7) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
```
---
###실습 publish
```
채널에 메시지 발행
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'

채널확인
pubsub channels
1) "1000"

pubsub channels
(empty array)


publish 2000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
publish admin01 "coaching1"
publish 1000 '{"callId":"20180901185935_58066","ext":"1101", ...}'
```
---
###실습2 subscribe
```
 subscribe admin01 1000

1) "subscribe"
2) "admin01"
3) (integer) 1
1) "subscribe"
2) "1000"
3) (integer) 2
1) "message"
2) "1000"
3) "{\"callId\":\"20180901185935_58066\",\"ext\":\"1101\", ...}"
1) "message"
2) "admin01"
3) "coaching1"
```
