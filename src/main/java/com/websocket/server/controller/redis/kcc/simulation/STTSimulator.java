package com.websocket.server.controller.redis.kcc.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class STTSimulator extends AbstractSimulator {

    @SneakyThrows
    public void load(String path) {
        // default resources file
        load(Api.STT, path+"/stt-sample.txt");
    }

    @Override
    protected Map<String, Object> getReGenerationValueMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("recKey", random(0, 10) + ".wav");
        return map;
    }

    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
