package com.websocket.server.controller.redis.kcc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

@Component("GlobalVariables")
public class GlobalVariables {

    public static ConcurrentHashMap<String,Integer> CHECK_CALL_STATUS_MAP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,String> REC_KEY_AND_CALL_ID_MAP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,String> CALL_ID_AND_AP_ID_MAP = new ConcurrentHashMap<>();

    public static int API_PROCESS_THREAD_COUNT;
    public static String STT_SPEAKER_DIVISION_EMPLOYEE;
    public static String STT_SPEAKER_DIVISION_CUSTOMER;

    @Value("${stt.speaker.division.employee:E}")
    public void setSttSpeakerDivisionEmployee(String sttSpeakerDivisionEmployee) {
        STT_SPEAKER_DIVISION_EMPLOYEE = sttSpeakerDivisionEmployee;
    }
    @Value("${stt.speaker.division.customer:C}")
    public void setSttSpeakerDivisionCustomer(String sttSpeakerDivisionCustomer) {
        STT_SPEAKER_DIVISION_CUSTOMER = sttSpeakerDivisionCustomer;
    }
    @Value("${api.process.thread.count:1}")
    public void setApiProcessThreadCount(int apiProcessThreadCount) {
        API_PROCESS_THREAD_COUNT = apiProcessThreadCount;
    }


    //////////////////////
    @Value("${infochatter.server-manager.ip:127.0.0.1}")
    public void setInfochatterIp(String ip){
        InfochatterEngineConstant.INFOCHATTER_IP = ip;
    }
    @Value("${infochatter.server-manager.port:7055}")
    public void setInfochatterPort(String port){
        InfochatterEngineConstant.INFOCHATTER_PORT = port;
    }
    @Value("${infochatter.server-manager.repo.id:AA}")
    public void setInfochatterRepoId(String repoId){
        InfochatterEngineConstant.INFOCHATTER_REPO_ID = repoId;
    }
    @Value("${infochatter.server-manager.agent.name:AA-AGENT-1}")
    public void setInfochatterAgentName(String agentName){
        InfochatterEngineConstant.INFOCHATTER_AGENT_NAME = agentName;
    }
    @Value("${okms.ip:127.0.0.1}")
    public void setOkmsIp(String okmsIp){
        OkmsConstant.OKMS_IP = okmsIp;
    }
    @Value("${okms.port:8080}")
    public void setOkmsPort(String okmsPort){
        OkmsConstant.OKMS_PORT = okmsPort;
    }

    @Value("${dqcat.server-manager.ip:127.0.0.1}")
    public void setDqcatServerManagerIp(String dqcatServerManagerIp) {
        DqcatConstant.DQCAT_SERVER_MANAGER_IP = dqcatServerManagerIp;
    }

    @Value("${dqcat.server-manager.port:4444}")
    public void setDqcatServerManagerPort(int dqcatServerManagerPort) {
        DqcatConstant.DQCAT_SERVER_MANAGER_PORT = dqcatServerManagerPort;
    }

    @Value("${dqcat.server-manager.collection-id:DP_CATEGORY}")
    public void setDqcatServerManagerCollectionId(String dqcatServerManagerCollectionId) {
        DqcatConstant.DQCAT_SERVER_MANAGER_COLLECTION_ID = dqcatServerManagerCollectionId;
    }

    @Value("${dqcat.server-manager.category-field:CATEGORY}")
    public void setDqcatServerManagerCategoryField(String dqcatServerManagerCategoryField) {
        DqcatConstant.DQCAT_SERVER_MANAGER_CATEGORY_FIELD = dqcatServerManagerCategoryField;
    }

    @Value("${dqcat.server-manager.result-size:5}")
    public void setDqcatServerManagerResultSize(String dqcatServerManagerResultSize) {
        DqcatConstant.DQCAT_SERVER_MANAGER_RESULT_SIZE = dqcatServerManagerResultSize;
    }
    
    @Value("${mariner.ip:127.0.0.1}")
    public void setMarinerIp(String marinerIp) {
    	MarinerConstant.MARINER_IP = marinerIp;
    }
    
    @Value("${mariner.port:51094}")
    public void setMarinerPort(int marinerPort) {
    	MarinerConstant.MARINER_PORT = marinerPort;
    }
    
    @Value("${mariner.collection:OKMS_DOC}")
    public void setMarinerCollection(String marinerCollection) {
    	MarinerConstant.MARINER_COLLECTION = marinerCollection;
    }

    @Value("${consult-qa.module.ip:127.0.0.1}")
    public void setConsultQaModuleIp(String consultQaModuleIp) {
        QaConstant.CONSULT_QA_MODULE_IP = consultQaModuleIp;
    }

    @Value("${consult-qa.module.port:7078}")
    public void setConsultQaModulePort(String consultQaModulePort) {
        QaConstant.CONSULT_QA_MODULE_PORT = consultQaModulePort;
    }

    @Value("${mariner.jiana.home:/resources/mariner4/jiana/korean/data}")
    public void setJianaHome(String jianaHome) {

        File path = new File(jianaHome);
        if(!path.exists()) {
            String defaultPath = "resources/mariner4/jiana/korean/data";
            StringBuilder pathTempSb = new StringBuilder(System.getProperty("user.dir"));
            for (String s : defaultPath.split("/")) {
                pathTempSb.append(System.getProperty("file.separator")).append(s);
            }
            jianaHome = pathTempSb.toString();
        }
        MarinerConstant.JIANA_HOME = jianaHome;
    }
    
    @Value("${redis.host:localhost}")
    public void setRedisHost(String redisHost) {
    	RedisConstant.REDIS_HOST = redisHost;
    }
    
    @Value("${redis.port:6379}")
    public void setRedisPort(int redisPort) {
    	RedisConstant.REDIS_PORT = redisPort;
    }

    @Value("${api.consult-advisor-restapi.ip:127.0.0.1}")
    public void setConsultAdvisorRestapiIp(String consultAdvisorRestapiIp) {
        ConsultAdvisorRestapiConstant.CONSULT_ADVISOR_RESTAPI_IP = consultAdvisorRestapiIp;
    }
    @Value("${api.consult-advisor-restapi.port:18080}")
    public void setConsultAdvisorRestapiPort(String consultAdvisorRestapiPort) {
        ConsultAdvisorRestapiConstant.CONSULT_ADVISOR_RESTAPI_PORT = consultAdvisorRestapiPort;
    }
    @Value("${api.consult-advisor-restapi.repo-agent:AA,AA-AGENT-1;TA,TA-AGENT-1}")
    public void setConsultAdvisorRestapiRepoAgent(String consultAdvisorRestapiRepoAgent) {
        ConsultAdvisorRestapiConstant.CONSULT_ADVISOR_RESTAPI_REPO_AGENT = consultAdvisorRestapiRepoAgent;
    }
    
    
    @Value("${simulation.host:localhost}")
    public void setSimulationHost(String simulationHost) {
    	SIMULATION.SIMULATION_HOST = simulationHost;
    }
    
    @Value("${simulation.port:6379}")
    public void setSimulationPort(int simulationPort) {
    	SIMULATION.SIMULATION_PORT = simulationPort;
    }
    
    @Value("${simulation.path:/simulation}")
    public void setSimulationPath(String simulationPath) {
    	SIMULATION.SIMULATION_PATH = simulationPath;
    }

    public static class InfochatterEngineConstant {
        public static String INFOCHATTER_IP;
        public static String INFOCHATTER_PORT;
        public static String INFOCHATTER_REPO_ID;
        public static String INFOCHATTER_AGENT_NAME;
        public static final String CHAT_CLASS = "com.diquest.infochatter.msg.ExtNetMsgChatting";
    }

    public static class OkmsConstant {
        public static String OKMS_IP;
        public static String OKMS_PORT;
    }

    public static class DqcatConstant {
        public static String DQCAT_SERVER_MANAGER_IP;
        public static int DQCAT_SERVER_MANAGER_PORT;
        public static String DQCAT_SERVER_MANAGER_COLLECTION_ID;
        public static String DQCAT_SERVER_MANAGER_CATEGORY_FIELD;
        public static String DQCAT_SERVER_MANAGER_RESULT_SIZE;
    }

    public static class MarinerConstant {
        public static String MARINER_IP;
        public static int MARINER_PORT;
        public static String MARINER_COLLECTION;
        public static String JIANA_HOME;
    }
    
    public static class RedisConstant {
        public static String REDIS_HOST;
        public static int REDIS_PORT;
    }

    public static class ConsultAdvisorRestapiConstant {
        public static String CONSULT_ADVISOR_RESTAPI_IP;
        public static String CONSULT_ADVISOR_RESTAPI_PORT;
        public static String CONSULT_ADVISOR_RESTAPI_REPO_AGENT;
    }
    
    public static class SIMULATION {
        public static String SIMULATION_HOST;
        public static int SIMULATION_PORT;
        public static String SIMULATION_PATH;
    }

    public static class QaConstant {
        public static String CONSULT_QA_MODULE_IP;
        public static String CONSULT_QA_MODULE_PORT;
    }

}
