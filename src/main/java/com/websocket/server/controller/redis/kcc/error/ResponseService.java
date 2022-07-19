package com.websocket.server.controller.redis.kcc.error;


import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Service // 해당 클래스가 Service임을 명시한다.

public class ResponseService {

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    // enum으로 API 요청 결과에 대한 code, message를 정의한다.
    public enum CommonResponse {
        SUCCESS("0000", "성공하였습니다. HappyPepe! XD"),
        FAIL("9999", "실패하였습니다. SadPepe :(");

        private String code;
        private String msg;
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() { // CommonResult 응답 결과를 알려주는 클래스
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    // 결과 모델에 API 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
    	result.setSuccess(true);
    	result.setReturn_code(CommonResponse.SUCCESS.getCode());
    	result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
    
    
    
    //==============실패 메소드
    
    public <T> SingleResult<T> getFailSingleResult() {
        SingleResult<T> result = new SingleResult<>();
//        result.setData(data);
        setFailResult(result);
        return result;
    }

    // 결과 모델에 API 요청 성공 데이터를 세팅해주는 메소드
    private void setFailResult(CommonResult result) {
    	result.setSuccess(false);
    	result.setReturn_code(CommonResponse.FAIL.getCode());
    	result.setMsg(CommonResponse.FAIL.getMsg());
    }
    
    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setSuccess(false); // setSuccess : 응답 성공 여부 (true/false)
        result.setReturn_code(CommonResponse.FAIL.getCode()); // setCode : 응답 코드 번호 >= 0 정상, < 0 비정상
        result.setMsg(CommonResponse.FAIL.getMsg()); // setMsg 응답 메시지
        return result;
    }

}