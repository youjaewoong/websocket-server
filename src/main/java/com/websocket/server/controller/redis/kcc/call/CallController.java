
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/call")
public class CallController {


	@Autowired
	private CallService callService;

	/**
	 * <p>
	 * 발행되는 메시지를 처리할 Listner
	 * 유형 : 조회
	 * IF요소 : 실시간 대화 연결
	 * </p>
	 * @param : stt
	 * @return : 실시간 STT 정보
	 */
	@PostMapping("/subAdvisor")
	public Set<String> subAdvisor() {
		log.info(">>># subAdvisor ext");
		return callService.findAllListener();
	}
	
	
	/**
	 * <p>
	 * 
	 * 
	 * 유형 : 조회
	 * 실행시점 : 조회시
	 * IF요소 : 상담정보 상세보기
	 * </p>
	 * @param : 콜ID
	 * @return : 콜상세 정보
	 */
	@PostMapping("/selectCallInfo")
	public void selectCallInfo(@RequestBody Object message) {
		log.info(">>># selectCallInfo : {}", message);
		try {
			callService.redisAddCallInfomation(message);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * <p>
	 * 통화가 연결된 고객정보를 확인한다.
	 * 유형 : 조회
	 * </p>
	 * @param : 상담원ID
	 * @return : 고객정보,가입상품정보,전화번호,통화시작시간
	 */
	@PostMapping("/selectClientInfo")
	public void selectClientInfo(@RequestBody Object message) {
		log.info(">>># selectClientInfo");
		try {
    		callService.redisAddClientInfomation(message);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * <p>
	 * stt 전송
	 * 유형 : 조회
	 * 실행시점 : 조회시
	 * IF요소 : 상담대화 상세보기
	 * </p>
	 * @param : 콜ID
	 * @return : STT 정보
	 */
	@PostMapping("/selectStt")
	public void selectStt(@RequestBody Object message) {
		log.info(">>># selectStt");
		try {
			
//			Map<String, Object> sttMap = ObjectUtil.ObjectToMap(message);
//			//인포채터로 stt 던지기
//			String infochatterAnswer = infochatterIntentAnalysisService.getInfochatterResultAnswer(sttMap.get("stt").toString());
//			System.out.println(infochatterAnswer);
//			// 인포채터 의도분석 저장
//			redisSaver.save(
//					RedisKeyType.HASH,
//					infochatterAnswer,
//					new String[]{"intend","callIdTest"},
//					new String[]{sttMap.get("dateTime").toString(),sttMap.get("dir").toString()}
//			);

			callService.redisAddStt(message);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
