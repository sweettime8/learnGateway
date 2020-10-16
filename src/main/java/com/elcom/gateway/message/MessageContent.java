package com.elcom.gateway.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import java.io.Serializable;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Admin
 */
public class MessageContent implements Serializable {

    private int status;
    private String message;
    private Object data;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public MessageContent() {
    }

    public MessageContent(Object data) {
        this.status = data != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value();
        this.message = data != null ? HttpStatus.OK.toString() : HttpStatus.NOT_FOUND.toString();
        this.data = data;
    }

    public MessageContent(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public MessageContent(int status, String message, Object data, Long total) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.total = total;
    }
    
    public String toJsonString(){
        return new Gson().toJson(this);
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Return total rows for paging
     * @return 
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param total for total rows paging
     */
    public void setTotal(Long total) {
        this.total = total;
    }
}
