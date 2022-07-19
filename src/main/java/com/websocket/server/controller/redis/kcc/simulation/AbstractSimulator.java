package com.websocket.server.controller.redis.kcc.simulation;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.server.controller.redis.kcc.GlobalVariables;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSimulator {

    protected ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> future;

    private String simulationHost = GlobalVariables.SIMULATION.SIMULATION_HOST;
    private int simulationPort = GlobalVariables.SIMULATION.SIMULATION_PORT;
    
    // base Url
    @Getter
    @Setter
    private String baseUrl = "http://"+simulationHost+":"+simulationPort;
    // key: targetUrl, list: data
    @Getter
    private final List<SimulatorEntity> datas = new ArrayList<>();

    /**
     * 시작
     */
    public void run() {
        System.out.println("[Simulation] 시작");
        future = executorService.scheduleAtFixedRate(
                new SimulatorRunnable(this), 0, 1, TimeUnit.SECONDS
        );
    }

    /**
     * 종료
     */
    public void stop() {
        System.out.println("[Simulation] 종료");
        future.cancel(true);
    }

    /**
     * 데이터 로드
     *
     * @param uri              target uri
     * @param fileResourcePath 파일 리소스 path
     */
    public void load(String uri, String fileResourcePath) {
        // default resources file
    	log.info("uri {}",uri);
    	log.info("fileResourcePath {}",fileResourcePath);
        fileToData(uri, fileResourcePath);
    }

    // DEV : heony_dev
    public void stringToData(String targetUri, String text) {
        // text data to map
        for (String line : text.split("\\r?\\n")) {
            datas.add(SimulatorEntity.builder()
                    .targetUri(targetUri)
                    .value(line)
                    .build());
        }
    }


    /**
     * 데이터 regeneration
     *
     * @return 매 루틴마다 변경할 필드 맵 (field, value)
     */
    protected abstract Map<String, Object> getReGenerationValueMap();

    /**
     * fileToData
     * <pre>
     *     file to data map list
     * </pre>
     *
     * @param filePath 파일 경로(resources 이하)
     */
    @SneakyThrows
    private void fileToData(String targetUri, String filePath) {
        // read file
//        File dir = new File(Objects.requireNonNull(
//                this.getClass().getResource(filePath)).getFile()
//        );
        File dir = new File(filePath);

        // file data to map
        for (String line : Files.readAllLines(dir.toPath())) {
            datas.add(SimulatorEntity.builder()
                    .targetUri(targetUri)
                    .value(line)
                    .build());
        }
    }

    /**
     * 데이터 regeneration
     * <pre>
     *     데이터 목록 중 매 루틴마다 신규 값으로 갱신되어야 할 경우 사용.
     * </pre>
     */
    @SneakyThrows
    private void dataRegeneration() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> reGenerationValueMap = getReGenerationValueMap();

        // regeneration 할 데이터가 없을 시 패스
        if (reGenerationValueMap == null || reGenerationValueMap.isEmpty()) {
            return;
        }

        for (String field : reGenerationValueMap.keySet()) {
            Object value = reGenerationValueMap.get(field);

            for (SimulatorEntity simulatorEntity : datas) {
                Map<String, Object> data = mapper.readValue(
                        simulatorEntity.getValue(),
                        new TypeReference<Map<String, Object>>() {
                        }
                );

                // 필드를 포함하지 않을 시 패스
                if (!data.containsKey(field)) {
                    continue;
                }

                data.put(field, value);
                simulatorEntity.setValue(mapper.writeValueAsString(data));
            }
        }
    }

    /**
     * API const
     */
    public static class Api {

//        public static String CALL_INFORMATION = "/ta_interface/callInformation";
//        public static String CLIENT_INFORMATION = "/ta_interface/clientInformation";
//        public static String STT = "/ta_interface/stt";
//        public static String CALL_HIST_INFO = "/ta_interface/callHistInfo";
//        public static String JOIN_PRD = "/ta_interface/joinPrd";
        public static String CALL_INFORMATION = "/call/selectCallInfo";
        public static String PRODUCT_INFORMATION = "/call/selectJoinProduct";
        public static String STT = "/call/selectStt";

    }

    /**
     * SimulatorRunnable inner class
     */
    private static class SimulatorRunnable implements Runnable {

        private final RestTemplate restTemplate = new RestTemplate();
        private final AbstractSimulator simulator;
        private int index;

        SimulatorRunnable(AbstractSimulator simulator) {
            this.simulator = simulator;
            index = 0;

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        }

        @Override
        public void run() {
            SimulatorEntity entity = simulator.getDatas().get(index);
            sendData(
                    simulator.getBaseUrl() + entity.getTargetUri(),
            		//simulator.getBaseUrl() +"/call/selectCallInfo",
                    entity.getValue()
            );

            index++;
//            if (index >= simulator.getDatas().size()) {
//                index = 0;
//                simulator.dataRegeneration();
//            }
        }

        /**
         * send Data
         *
         * @param url  대상 url
         * @param data 보낼 데이터
         */
        @SneakyThrows
        private void sendData(String url, Object data) {
            System.out.printf("send Data to [%s]: %s%n", url, data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(data, headers);

            restTemplate.postForObject(url, request, String.class);
        }
    }

    /**
     * simulation data record
     */
    @Data
    @Builder
    private static class SimulatorEntity {
        private String targetUri;
        private String value;
    }

}
