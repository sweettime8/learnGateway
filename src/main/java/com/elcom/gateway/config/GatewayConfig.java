/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.config;

import com.elcom.gateway.model.dto.RabbitMqType;
import com.elcom.gateway.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class GatewayConfig {

    //Typesafe config
    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;

    //Constant root api path
    public static final String API_ROOT_PATH = "/v1.0/";
    
    //Time out khi call RPC (s)
    public static Integer RPC_TIMEOUT = 5;
    
    //List mã dịch vụ
    public static List<String> SERVICE_LIST = new ArrayList<>();
    
    //Map chứa các key,value ngoài các map định nghĩa bên dưới
    public static final Map<String, String> SERVICE_MAP = new HashMap<>();
    
    //Map chứa list path private
    public static final Map<String, List<String>> SERVICE_PATH_PRIVATE_MAP = new HashMap<>();
    
    //Map chứa list path
    public static final Map<String, List<String>> SERVICE_PATH_MAP = new HashMap<>();
    
    //Map chứa kiểu rabbit (RPC, Worker, Pub/Sub) cho path + method
    public static final Map<String, String> RABBIT_TYPE_MAP = new HashMap<>();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            System.out.println("======= Loading GatewayConfig config... =======");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "gateway.properties"), "UTF-8"));
            Enumeration e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                
                System.out.println("+ key: " + key + " => value: " + value);
                if (key.equalsIgnoreCase("rpc.timeout")) {
                    if(StringUtil.isNumberic(value)){
                        RPC_TIMEOUT = Integer.parseInt(value.trim());
                    }
                } else if (key.equalsIgnoreCase("service.list")) {
                    String[] arrDomain = value.split(",");
                    SERVICE_LIST = Arrays.asList(arrDomain);
                } else {
                    SERVICE_MAP.put(key, value);
                    if (key.contains(".path.rabbit.file")) {
                        //Load rabbit json file
                        loadRabbitJson(value);
                    } else if (key.contains(".path.private")) {
                        String[] arrPrivatePath = value.split(",");
                        List<String> pathPrivateList = Arrays.asList(arrPrivatePath);
                        SERVICE_PATH_PRIVATE_MAP.put(key.replace(".path.private", ""), pathPrivateList);
                    } else if (key.contains(".path")) {
                        String[] arrPath = value.split(",");
                        List<String> pathList = Arrays.asList(arrPath);
                        SERVICE_PATH_MAP.put(key.replace(".path", ""), pathList);
                    }
                }
            }
            System.out.println("=== Load GatewayConfig config successfull!!! ===");
        } catch (IOException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void loadRabbitJson(String jsonFile) {
        try {
            FileReader reader = new FileReader(CONFIG_DIR + jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            List<RabbitMqType> rabbitMqTypeList = mapper.readValue(reader, new TypeReference<List<RabbitMqType>>() {
            });
            if (rabbitMqTypeList != null && rabbitMqTypeList.size() > 0) {
                rabbitMqTypeList.forEach((tmp) -> {
                    System.out.println(tmp.getMethod() + " " + tmp.getPath() + " => " + tmp.getRabbit());
                    RABBIT_TYPE_MAP.put(tmp.getMethod() + " " + tmp.getPath(), tmp.getRabbit());
                });
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GatewayConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void main(String[] args) {
//        System.out.println("RPC_TIMEOUT: " + RPC_TIMEOUT);
//        
//        List<String> userPathList = SERVICE_PATH_MAP.get("user");
//        userPathList.forEach((path) -> {
//            System.out.println(path);
//        });
//        String requestPath = "/user";
//        System.out.println("Contain path " + requestPath + " ==> " + userPathList.contains(requestPath));
//        String rabbitType = RABBIT_TYPE_MAP.get("GET /user");
//        System.out.println("rabbitType for GET /user: " + rabbitType);
//
//        //list
//        String service = "user";
//        String rpcQueue = GatewayConfig.SERVICE_MAP.get(service + ".rpc.queue");
//        String rpcExchange = GatewayConfig.SERVICE_MAP.get(service + ".rpc.exchange");
//        String rpcKey = GatewayConfig.SERVICE_MAP.get(service + ".rpc.key");
//        String workerQueue = GatewayConfig.SERVICE_MAP.get(service + ".worker.queue");
//        String directQueue = GatewayConfig.SERVICE_MAP.get(service + ".direct.queue");
//        String directExchange = GatewayConfig.SERVICE_MAP.get(service + ".direct.exchange");
//        String directKey = GatewayConfig.SERVICE_MAP.get(service + ".direct.key");
//
//        System.out.println(rpcExchange + "," + rpcQueue + "," + rpcKey + "\n" + workerQueue
//                + "\n" + directExchange + "," + directQueue + "," + directKey);
//    }
}
