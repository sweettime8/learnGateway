package com.elcom.gateway.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Admin
 */
public class ResponseMessage {

    private int status;
    private String message;
    private MessageContent data;

    public ResponseMessage() {
    }
    
    public ResponseMessage(MessageContent data) {
        this.status = data != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value();
        this.message = data != null ? HttpStatus.OK.toString() : HttpStatus.NOT_FOUND.toString();
        this.data = data;
    }

    public ResponseMessage(int status, String message, MessageContent data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public MessageContent getData() {
        return data;
    }

    public void setData(MessageContent data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //mapper.setDateFormat(df);
        return mapper.writeValueAsString(this);
    }
}
