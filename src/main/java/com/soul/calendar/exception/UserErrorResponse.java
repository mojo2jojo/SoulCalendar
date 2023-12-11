package com.soul.calendar.exception;

import java.sql.Timestamp;

public class UserErrorResponse {
    private Integer status;
    private String message;
    private Timestamp timestamp;

    public UserErrorResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "UserErrorResponse [status=" + status + ", message=" + message + ", timestamp=" + timestamp + "]";
    }

    public UserErrorResponse(Integer status, String message, Timestamp timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
