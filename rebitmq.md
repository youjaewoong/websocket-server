```
[rabbitmq install]
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 --restart=unless-stopped rabbitmq:management

[rabbitmq 실행]
http://localhost:15672/

[rabbitmq id/pw]
guest/guest

[참조]
https://musclebear.tistory.com/139
```