package com.websocket.server.controller.redis.kcc.call;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import com.websocket.server.controller.redis.kcc.GlobalVariables;

@Slf4j
public class CallCheckService {

    /*
    * GlobalVariables.CHECK_CALL_STATUS_MAP의 각 key의 value가 2가 되면
    * 콜 종료신호를 주면서 그 key는 삭제
    * */
    
    // 실행 메소드
    public static boolean isEndCall(String key, Map<String, Object> sttMap){
        log.debug("### : key : " + key);
        log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
        // 0. 해당 신호가 END신호인지 체크
        if(_isEndSignal(sttMap)){
            log.debug("==================================END SIGNAL========================================");
            // 1. 해당 키가 존재하는지 확인 _isExist
            if(_isExist(key)){
                // 1-1. 해당 키가 존재한다면, countUp
                countUp(key);
                log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
                // 1-1-1. 카운트 업 후 체크
                if(_isEnd(key)){
                    clear(key);
                    log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
                    return true;
                }
            }else{
                // 1-2. 해당 키가 존재하지 않다면, create
                create(key);
                log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
            }
            // 2. 해당 키의 값이 2인지 체크 (_isEnd)
//            if(_isEnd(key)){
//                log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
//                // 2-1. 1보다 크면(2이면) clear(key)후 return true;
//                clear(key);
//                log.debug("### : value : " + GlobalVariables.CHECK_CALL_STATUS_MAP.get(key));
//                return true;
//            }
            // 2-2. 아니면 아무것도 안함
        }
        return false;
    }



    private static boolean _isEndSignal(Map<String, Object> sttMap){
        if(sttMap.get("index")==null){
            return false;
        }else{
            return sttMap.get("index").toString()!=null && sttMap.get("index").toString().equals("E");
        }
    }

    private static void clear(String key){
        GlobalVariables.CHECK_CALL_STATUS_MAP.remove(key);
    }

    private static void create(String key){
        GlobalVariables.CHECK_CALL_STATUS_MAP.put(key,1);
    }

    private static void countUp(String key){
        GlobalVariables.CHECK_CALL_STATUS_MAP.put(key,GlobalVariables.CHECK_CALL_STATUS_MAP.get(key)+1);
    }

    private static boolean _isEnd(String key){
        if(GlobalVariables.CHECK_CALL_STATUS_MAP.get(key)>1) return true;
        return false;
    }

    private static boolean _isExist(String key){
        if(GlobalVariables.CHECK_CALL_STATUS_MAP.get(key)!=null) return true;
        return false;
    }


}
