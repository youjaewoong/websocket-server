package com.websocket.server.controller.redis.kcc.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.context.annotation.PropertySource;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
@PropertySource("classpath:/config.properties")
public class TotalSimulator extends AbstractSimulator {

    // DEV : heony_dev
    public void send(String callInfoJson, String joinProductJson, String sttSampleJsonCollection){
        stringToData(Api.CALL_INFORMATION,callInfoJson);
        stringToData(Api.PRODUCT_INFORMATION,joinProductJson);
        stringToData(Api.STT,sttSampleJsonCollection);
    }

    @SneakyThrows
    public void load(String path) {
        System.out.println(path);

        // default resources file
        // 상담통화연결정보
        load(Api.CALL_INFORMATION, path+"/call-info-sample.txt");
        // 가입상품
        load(Api.PRODUCT_INFORMATION, path+"/joinProduct-sample.txt");
        // STT
        load(Api.STT, path+"/stt-sample.txt");
        // 상담이력정보
        // TODO
        // 가입상품
        // TODO
    }

    @Override
    protected Map<String, Object> getReGenerationValueMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("recKey", random(0, 100) + ".wav");
        return map;
    }

    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
