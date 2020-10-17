/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.controller;

import com.elcom.gateway.config.GatewayConfig;
import com.elcom.gateway.config.IpBlackConfig;
import com.elcom.gateway.exception.ValidationException;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.gateway.messaging.rabbitmq.RabbitMQClient;
import com.elcom.gateway.utils.StringUtil;
import com.elcom.gateway.validation.GatewayValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Admin
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private IpBlackConfig ipBlackConfig;
    
    public ResponseEntity<String> processRequest(String requestMethod, Map<String, String> urlParamMap,
            Map<String, Object> bodyParamMap, Map<String, String> headerParamMap,
            HttpServletRequest req) throws JsonProcessingException, MalformedURLException, IOException {
        //Chan IP if IP in IPBlackList
        //get current ip
        URL url = new URL("http://api.ipify.org");
        String ipaddress = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        for (String line; (line = reader.readLine()) != null;) {
            ipaddress = line;
        }
        Pattern p;
        //check ip có trong black list không
        for (int i = 0; i < ipBlackConfig.getLstIp().size(); i++) {
            p = Pattern.compile("^(?:" + ipBlackConfig.getLstIp().get(i).replaceAll("X",
                    "(?:\\\\d{1,2}|1\\\\d{2}|2[0-4]\\\\d|25[0-5])") + ")$");
            Matcher m = p.matcher(ipaddress);
            if (m.matches()) {
                //IP is locked ,  return bad request
                return new ResponseEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
            }
        }
        //end check IP

        //Get all value
        String requestPath = req.getRequestURI();
        String urlParam = StringUtil.generateMapString(urlParamMap);
        String pathParam = null;

        //Service
        int index = requestPath.indexOf("/", GatewayConfig.API_ROOT_PATH.length());
        String service = null;
        if (index != -1) {
            service = requestPath.substring(GatewayConfig.API_ROOT_PATH.length(), index);
        } else {
            service = requestPath.replace(GatewayConfig.API_ROOT_PATH, "");
        }

        //Check has path param
        int lastIndex = requestPath.lastIndexOf("/");
        if (lastIndex != -1) {
            String lastStr = requestPath.substring(lastIndex + 1);
            if (StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)) {
                requestPath = requestPath.substring(0, lastIndex);
                pathParam = lastStr;
            }
        }

        //Log request info
        LOGGER.info("[{}] to requestPath: {} - urlParam: {} - pathParm: {} - bodyParam: {} - headerParam: {}",
                requestMethod, requestPath, urlParam, pathParam, bodyParamMap, StringUtil.generateMapString(headerParamMap));
        //Validate
        new GatewayValidation().validate(requestPath, service);

        RequestMessage request = new RequestMessage(requestMethod, requestPath, urlParam,
                pathParam, bodyParamMap, headerParamMap);
        String result = null;

        //Get rabbit type
        String rabbitType = GatewayConfig.RABBIT_TYPE_MAP.get(requestMethod + " "
                + requestPath.replace(GatewayConfig.API_ROOT_PATH, "/"));
        LOGGER.info("Get Rabbit type for {} {} ==> Rabbit: {}", requestMethod,
                requestPath.replace(GatewayConfig.API_ROOT_PATH, "/"), rabbitType);
        if ("rpc".equalsIgnoreCase(rabbitType)) {
            String rpcQueue = GatewayConfig.SERVICE_MAP.get(service + ".rpc.queue");
            String rpcExchange = GatewayConfig.SERVICE_MAP.get(service + ".rpc.exchange");
            String rpcKey = GatewayConfig.SERVICE_MAP.get(service + ".rpc.key");
            if (StringUtil.isNullOrEmpty(rpcQueue) || StringUtil.isNullOrEmpty(rpcExchange) || StringUtil.isNullOrEmpty(rpcKey)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            result = rabbitMQClient.callRpcService(rpcExchange, rpcQueue, rpcKey, request.toJsonString());
            LOGGER.info("result: " + result);
        } else if ("worker".equalsIgnoreCase(rabbitType)) {
            String workerQueue = GatewayConfig.SERVICE_MAP.get(service + ".worker.queue");
            if (StringUtil.isNullOrEmpty(workerQueue)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            //Call worker
            if (rabbitMQClient.callWorkerService(workerQueue, request.toJsonString())) {
                MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), mc);
                result = responseMessage.toJsonString();
            } else {
                MessageContent mc = new MessageContent(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), "Error");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), mc);
                result = responseMessage.toJsonString();
            }
        } else if ("publish".equalsIgnoreCase(rabbitType)) {
            String directExchange = GatewayConfig.SERVICE_MAP.get(service + ".direct.exchange");
            String directKey = GatewayConfig.SERVICE_MAP.get(service + ".direct.key");
            if (StringUtil.isNullOrEmpty(directExchange) || StringUtil.isNullOrEmpty(directKey)) {
                throw new ValidationException("Không tìm thấy rabbit mq cho service " + service);
            }
            //Call publisher
            if (rabbitMQClient.callPublishService(directExchange, directKey, request.toJsonString())) {
                MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), mc);
                result = responseMessage.toJsonString();
            } else {
                MessageContent mc = new MessageContent(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), "Error");
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.toString(), mc);
                result = responseMessage.toJsonString();
            }
        } else {
            MessageContent mc = new MessageContent(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), "Error");
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), mc);
            result = responseMessage.toJsonString();
            throw new ValidationException("Không tìm thấy xử lý cho kiểu rabbit " + rabbitType);
        }
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //mapper.setDateFormat(df);
            ResponseMessage response = mapper.readValue(result, ResponseMessage.class);
            return new ResponseEntity(response.getData(), HttpStatus.valueOf(response.getStatus()));
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
    }
}
