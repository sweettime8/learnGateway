/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.model.dto;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class ResponseData implements Serializable {

    private int status;

    private Object data;

    public ResponseData(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
