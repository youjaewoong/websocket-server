package com.websocket.server.controller.redis.kcc.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonStringUtil {

    public static String setUrlWithoutQueryParams(String url){
        return setUrlWithQueryParams(url,null);
    }

    public static String setUrlWithoutQueryParams(String ip, String port){
        return setUrlWithQueryParams(ip+":"+port,null);
    }

    public static String setUrlWithQueryParams(String ip, String port, Map<String,String> queryParamKeyValueMap){
        return setUrlWithQueryParams(ip+":"+port,queryParamKeyValueMap);
    }
    public static String setUrlWithQueryParams(String ip, String port,String uri, Map<String,String> queryParamKeyValueMap){
        if(uri.charAt(0)!='/') uri = "/"+uri;
        return setUrlWithQueryParams(ip+":"+port+uri,queryParamKeyValueMap);
    }

    public static String setUrlWithQueryParams(String url, Map<String,String> queryParamKeyValueMap){
        if(url==null || url.length()<7) {
            return url;
        }
        StringBuilder urlStringBuilder = new StringBuilder(url);
        if(!url.substring(0,7).equals("http://")){
            urlStringBuilder.insert(0,"http://");
        }
        if(queryParamKeyValueMap!=null&&queryParamKeyValueMap.size()>0){
            urlStringBuilder.append("?");
            queryParamKeyValueMap.forEach((key, value) -> {
                if(value!=null) urlStringBuilder.append(key).append("=").append(value).append("&");
            });
            urlStringBuilder.deleteCharAt(urlStringBuilder.length()-1);
        }
        return urlStringBuilder.toString();
    }

    public static void deleteLastCharStringBuilder(StringBuilder sb){
        if(sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
    }

    public static Map<String,Object> jsonStringToMap(String jsonString){
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        try {
            Map<String, Object> returnMap = new ObjectMapper().readValue(jsonString, typeRef);
            return returnMap;
        } catch (JsonProcessingException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public static String collectKeywordinMapinListToStringDelimComma(List<Object> list){
        StringBuilder interestKeywordSb = new StringBuilder();
        for (Object o : list) {
            Map<String,String> map = new ObjectMapper().convertValue(o,Map.class);
            interestKeywordSb.append(map.get("keyword")).append(",");
        }
        CommonStringUtil.deleteLastCharStringBuilder(interestKeywordSb);
        return interestKeywordSb.toString();
    }

}
