/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/v1.0/")
public class GatewayController extends BaseController {

    //GET
    @RequestMapping(value = "**", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMethod(@RequestParam Map<String, String> reqParam,
            @RequestHeader Map<String, String> headers, HttpServletRequest req) throws JsonProcessingException, IOException {
        return processRequest("GET", reqParam, null, headers, req);
    }

    //POST
    @RequestMapping(value = "**", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMethod(@RequestParam Map<String, String> reqParam,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException, IOException {
        return processRequest("POST", reqParam, requestBody, headers, req);
    }

    //PUT
    @RequestMapping(value = "**", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putMethod(@RequestParam Map<String, String> reqParam,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException, IOException {
        return processRequest("PUT", reqParam, requestBody, headers, req);
    }

    //PATCH
    @RequestMapping(value = "**", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchMethod(@RequestParam Map<String, String> reqParam,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException, IOException {
        return processRequest("PATCH", reqParam, requestBody, headers, req);
    }

    //DELETE
    @RequestMapping(value = "**", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMethod(@RequestParam Map<String, String> reqParam,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException, IOException {
        return processRequest("DELETE", reqParam, requestBody, headers, req);
    }
}
