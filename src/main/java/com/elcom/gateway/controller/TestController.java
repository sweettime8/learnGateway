/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.controller;

import com.elcom.gateway.config.IpBlackConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
public class TestController {
    @Autowired
    private IpBlackConfig ipBlackConfig;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> test() throws MalformedURLException, IOException{
                URL url = new URL("http://api.ipify.org");
        String ipaddress = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        for (String line; (line = reader.readLine()) != null;) {
            ipaddress = line;
        }
        Pattern p;
        //check ip có trong black list không
        boolean checkIp = false;
        for (int i = 0; i < ipBlackConfig.getLstIp().size(); i++) {
            p = Pattern.compile("^(?:" + ipBlackConfig.getLstIp().get(i).replaceAll("X",
                    "(?:\\\\d{1,2}|1\\\\d{2}|2[0-4]\\\\d|25[0-5])") + ")$");
            Matcher m = p.matcher(ipaddress);
            if (m.matches()) {
                //ip bi chan -> checkIP = true 
                checkIp = true;
                System.out.println("IP " + ipaddress + " bi chan boi ip black : " + ipBlackConfig.getLstIp().get(i));
                break;
            } else {
                System.out.println("IP " + ipaddress + " ko bi chan boi ip black : " + ipBlackConfig.getLstIp().get(i));
            }
        }
        
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
