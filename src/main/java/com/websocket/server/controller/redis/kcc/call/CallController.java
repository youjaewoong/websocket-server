
/**
 * @filename : Call.java
 * @deprecated : 
 * @author Dell
 * @version : 
 */

package com.websocket.server.controller.redis.kcc.call;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.controller.redis.kcc.error.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"실시간 데이터 수신 및 Publich하는  Controller"})
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/call")
public class CallController {

	@Autowired
	private ResponseService responseService; // 결과를 처리하는 Service
	
	@Autowired
	private CallService callService;
	

	/**
	 * 발행되는 메시지를 처리할 Listner
	 * @param : stt
	 * @return : 실시간 STT 정보
	 */
	@ApiResponse(code = 0000, message = "구독중인 전체 Listener 리턴")
    @ApiOperation(value = "발행되는 메시지를 처리할 Listner")
	@PostMapping("/subAdvisor")
	public Set<String> subAdvisor() {
		log.info(">>># subAdvisor ext");
		return callService.findAllListener();
	}
	
	
	/**
	 * 상담 통화 연결 정보 수신
	 * @param : 콜ID
	 * @return : 콜상세 정보
	 */
	@ApiResponse(code = 0000, message = "상담 통화 연결 정보 수신 확인 코드")
    @ApiOperation(value = "상담 통화 연결 정보")
	@PostMapping("/selectCallInfo")
	public String selectCallInfo(@RequestBody Object message) {
		log.info(">>># selectCallInfo : {}", message);
		try {
			callService.redisAddCallInfomation(message);
			return responseService.getSuccessResult().toString();
		} catch (Exception e) {
			log.error(e.toString());
			return responseService.getFailResult().toString();
		}
	}
	

	
	/**
	 * stt 전송
	 * @param : STT 정보
	 * @return : STT 정보
	 */
	@ApiResponse(code = 0000, message = "STT 정보 수신 확인 코드")
    @ApiOperation(value = "STT")
	@PostMapping("/selectStt")
	public String selectStt(@RequestBody Object message) {
		log.info(">>># selectStt");
		try {
			callService.redisAddStt(message);
			return responseService.getSuccessResult().toString();
		} catch (Exception e) {
			log.error(e.toString());
			return responseService.getFailResult().toString();
		}
	}
	


	
	/**
	 * 고객의 최근 상담 이력 전송
	 * @param :  최근 상담 이력
	 * @return :  최근 상담 이력
	 */
	@ApiResponse(code = 0000, message = "최근상담이력 수신 확인 코드")
    @ApiOperation(value = "최근상담이력")
	@PostMapping("/selectCallHistory")
	public String selectCallHistory(@RequestBody Object message) {
		log.info(">>># selectCallHistory");
		try {
    		return responseService.getSuccessResult().toString();
		} catch (Exception e) {
			log.error(e.toString());
			return responseService.getFailResult().toString();
		}
	}
}
