package com.soul.calendar.exception;

import java.sql.Timestamp;

public class ErrorResponse {

    public ErrorResponse(int status, String message, Timestamp timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    private int status;

    private String message;

    private Timestamp timestamp;

    public ErrorResponse() {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
