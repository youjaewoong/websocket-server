package com.websocket.server.controller.redis.kcc.model;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.websocket.server.controller.redis.kcc.GlobalVariables;

@Data
public class SttInfo {

    String startTime;
    String endTime;
    String who;
    String sttText;

    public SttInfo(String sttRaw, String ioGbn, String dir){
        sttRaw = sttRaw.trim();
        String startTime = "0";
        String endTime = "0";
        String who = GlobalVariables.STT_SPEAKER_DIVISION_EMPLOYEE;

        if(ioGbn!=null){
            if(ioGbn.equalsIgnoreCase("IB")){
                if(dir.equalsIgnoreCase("T")){
                    //상담원
                    who = GlobalVariables.STT_SPEAKER_DIVISION_EMPLOYEE;
                }else{  //R
                    //고객
                    who = GlobalVariables.STT_SPEAKER_DIVISION_CUSTOMER;
                }
            }else{  //OB
                if(dir.equalsIgnoreCase("T")){
                    //고객
                    who = GlobalVariables.STT_SPEAKER_DIVISION_CUSTOMER;
                }else{  //R
                    //상담원
                    who = GlobalVariables.STT_SPEAKER_DIVISION_EMPLOYEE;
                }
            }
        }else{
            if(dir.equalsIgnoreCase("T")){
                //상담원
                who = GlobalVariables.STT_SPEAKER_DIVISION_EMPLOYEE;
            }else{  //R
                //고객
                who = GlobalVariables.STT_SPEAKER_DIVISION_CUSTOMER;
            }
        }

        StringBuilder sttText = new StringBuilder();
        if(sttRaw!=null&&sttRaw.length()>0){
            if(sttRaw.contains("|")){
                startTime = sttRaw.split("\\|")[1];
                Matcher m = Pattern.compile("\\r?\\n").matcher(sttRaw);

                int lineSeparateCnt = 0;
                while (m.find()){
                    lineSeparateCnt++;
                }
                if(lineSeparateCnt>0){
                    if(sttRaw.split("\\r?\\n").length>0){
                        String lastSentence = sttRaw.split("\\r?\\n")[sttRaw.split("\\r?\\n").length-1];
                        if(lastSentence.contains("|")) endTime = lastSentence.split("\\|")[1];
                    }
                }else{
                    // 0000|0000|ㅁㄴㅇㄻㄴㅇㄹ|ㅁ
                    if(sttRaw.contains("|")){
                        startTime = sttRaw.split("\\|")[0];
                        endTime = sttRaw.split("\\|")[1];
                    }
                }
            }

            Matcher m = Pattern.compile("([ㄱ-ㅎ]|[ㅏ-ㅑ]|[가-힣])+").matcher(sttRaw);
            while (m.find()){
                sttText.append(m.group()).append(" ");
            }

        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.sttText = sttText.toString();
        this.who = who;
    }

    @Override
    public String toString(){
        return String.format("%s|%s|%s|%s",startTime,endTime,sttText,who);
    }

    public String toStringSttOnly(){
        return sttText;
    }

}
