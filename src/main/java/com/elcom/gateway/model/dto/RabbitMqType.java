/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.model.dto;

/**
 *
 * @author Admin
 */
public class RabbitMqType {
    private String path;
    private String method;
    private String rabbit;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRabbit() {
        return rabbit;
    }

    public void setRabbit(String rabbit) {
        this.rabbit = rabbit;
    }
    
}
