/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author admin
 */
@ConfigurationProperties("blacklist")
@Component
public class IpBlackConfig {
    private List<String> lstIp;

    public List<String> getLstIp() {
        return lstIp;
    }

    public void setLstIp(List<String> lstIp) {
        this.lstIp = lstIp;
    }
    
}
