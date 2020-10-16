/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.validation;

import com.elcom.gateway.config.GatewayConfig;
import com.elcom.gateway.exception.ValidationException;
import com.elcom.gateway.utils.StringUtil;
import java.util.List;

/**
 *
 * @author Admin
 */
public class GatewayValidation extends AbstractValidation {

    public void validate(String requestPath, String service) throws ValidationException {
        //Request uri
        if (StringUtil.isNullOrEmpty(requestPath)) {
            getMessageDes().add("Path request không được trống");
        }
        //Service
        if(StringUtil.isNullOrEmpty(service) || !GatewayConfig.SERVICE_LIST.contains(service)){
            getMessageDes().add("Service request không tồn tại");
        }
        //Path replace root
        requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH) 
                ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;
        //Check has path param
        int index = requestPath.lastIndexOf("/");
        if(index != -1){
            String lastStr = requestPath.substring(index + 1);
            if(StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)){
                requestPath = requestPath.substring(0, index);
            }
        }
        //Check exist path
        List<String> pathList = GatewayConfig.SERVICE_PATH_MAP.get(service);
        if(pathList == null || pathList.isEmpty() || !pathList.contains(requestPath)){
            getMessageDes().add("Request path không tồn tại");
        }
        //Check path private => reject
        if(GatewayConfig.SERVICE_PATH_PRIVATE_MAP.containsKey(service)){
            List<String> pathPrivateList = GatewayConfig.SERVICE_PATH_PRIVATE_MAP.get(service);
            boolean isPrivate = false;
            if(pathPrivateList != null && !pathPrivateList.isEmpty()){
                for(String tmpPath : pathPrivateList){
                    if(tmpPath.equals(requestPath)){
                        isPrivate = true;
                        break;
                    }
                }
            }
            if(isPrivate){
                getMessageDes().add("Request path không mở public");
            }
        }
        if (!isValid()) {
            throw new ValidationException(buildValidationMessage());
        }
    }

}
