package com.websocket.server.controller.redis.kcc.simulation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.websocket.server.controller.redis.kcc.GlobalVariables;
import com.websocket.server.controller.redis.kcc.util.CommonStringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"시뮬레이션 Controller"})
@RestController
@RequestMapping(value = "/simulation")
@Slf4j
public class SttSimulationController {

    private String host = GlobalVariables.SIMULATION.SIMULATION_HOST;
    private int port = GlobalVariables.SIMULATION.SIMULATION_PORT;
    private String path =  GlobalVariables.SIMULATION.SIMULATION_PATH;

    private final Map<String, STTSimulator> STTSimulatorPool = new HashMap<>();
    private final Map<String, TotalSimulator> totalSimulatorPool = new HashMap<>();

    /**
     * stt data load
     */
    @ApiOperation(value = "stt 시뮬레이션 시작 메소드")
    @GetMapping("/stt/start")
    public void sttStart(@RequestParam(value = "key", required = false, defaultValue = "01") String key) {
        // 이미 구동중일 경우 재시작
        if (STTSimulatorPool.containsKey(key)) {
            STTSimulatorPool.get(key).stop();
            STTSimulatorPool.get(key).run();
        } else {
        	log.info("BaseUrl {}", "http://"+host+":" + port);
        	log.info("path {}", path);
            STTSimulator simulator = new STTSimulator();
            simulator.setBaseUrl("http://"+host+":" + port);
            simulator.load(path);
            simulator.run();
            STTSimulatorPool.put(key, simulator);
        }
    }

    /**
     * stt data load
     */
    @ApiOperation(value = "stt 시뮬레이션 종료 메소드")
    @GetMapping("/stt/stop")
    public void sttStop(@RequestParam(value = "key", required = false, defaultValue = "01") String key) {
        STTSimulatorPool.get(key).stop();
        STTSimulatorPool.remove(key);
    }

    /**
     * total data load
     */
    @ApiOperation(value = "전체 시뮬레이션 시작 메소드")
    @GetMapping("/total/start")
    public void totalStart(@RequestParam(value = "key", required = false, defaultValue = "01") String key) {
        // 이미 구동중일 경우 재시작
        if (totalSimulatorPool.containsKey(key)) {
            totalSimulatorPool.get(key).stop();
            totalSimulatorPool.get(key).run();
        } else {
        	log.info("BaseUrl {}", "http://"+host+":" + port);
        	log.info("path {}", path);
            TotalSimulator simulator = new TotalSimulator();
            simulator.setBaseUrl("http://"+host+":" + port);
            simulator.load(path);
            simulator.run();
            totalSimulatorPool.put(key, simulator);
        }
    }

    /**
     * total data load
     */
    @ApiOperation(value = "전체 시뮬레이션 종료 메소드")
    @GetMapping("/total/stop")
    public void totalStop(@RequestParam(value = "key", required = false, defaultValue = "01") String key) {
        totalSimulatorPool.get(key).stop();
        totalSimulatorPool.remove(key);
    }

    // DEV : heony_dev
    @ApiOperation(value = "파일업로드 후 시뮬레이션 메소드")
    @PostMapping("/total/start/file")
    public void totalStart(@RequestParam(value = "key",required = false) String extNo,
                           @RequestPart("call-info") MultipartFile callInfo,
                           @RequestPart("stt-sample") MultipartFile sttSample,
                           @RequestPart("joinProduct-sample") MultipartFile joinProduct) throws IOException {
        log.info("### extNo : " + extNo);
        InputStreamReader callInfoisr = new InputStreamReader(callInfo.getInputStream());
        BufferedReader callInfoBuffer = new BufferedReader(callInfoisr);
        StringBuilder callInfoSb = new StringBuilder();

        InputStreamReader joinProductIsr = new InputStreamReader(joinProduct.getInputStream());
        BufferedReader joinProductBuffer = new BufferedReader(joinProductIsr);
        StringBuilder joinProductSb = new StringBuilder();

        InputStreamReader sttIsr = new InputStreamReader(sttSample.getInputStream());
        BufferedReader sttBuffer = new BufferedReader(sttIsr);
        StringBuilder sttSampleSb = new StringBuilder();

        String temp;
        
        //상담 통화 연결 정보
        while((temp = callInfoBuffer.readLine()) != null){
            callInfoSb.append(temp).append("\n");
        }
        CommonStringUtil.deleteLastCharStringBuilder(callInfoSb);
        
        //가입상품
        while((temp = joinProductBuffer.readLine()) != null){
        	joinProductSb.append(temp).append("\n");
        }
        CommonStringUtil.deleteLastCharStringBuilder(joinProductSb);

        temp=null;
        //stt
        while((temp = sttBuffer.readLine()) != null){
            sttSampleSb.append(temp).append("\n");
        }
        CommonStringUtil.deleteLastCharStringBuilder(sttSampleSb);

        if(StringUtils.isEmpty(extNo)){
            JSONParser parser = new JSONParser();
            Object obj = null;
            try {
                obj = parser.parse( callInfoSb.toString() );
                JSONObject jsonObj = (JSONObject) obj;
                extNo = jsonObj.get("ext")==null?"":jsonObj.get("ext").toString();
            } catch (ParseException e) {
                log.error(e.toString());
                extNo=""+System.currentTimeMillis();
            }
        }
        callInfoisr.close();
        callInfoBuffer.close();
        joinProductIsr.close();
        joinProductBuffer.close();
        sttIsr.close();
        sttBuffer.close();

        log.info("BaseUrl {}", "http://"+host+":" + port);
        log.info("path {}", path);
        TotalSimulator simulator = new TotalSimulator();
        simulator.setBaseUrl("http://"+host+":" + port);
        simulator.send(
        		callInfoSb.toString()
        		, joinProductSb.toString()
        		, sttSampleSb.toString());
        simulator.run();
        totalSimulatorPool.put(extNo, simulator);
    }



}