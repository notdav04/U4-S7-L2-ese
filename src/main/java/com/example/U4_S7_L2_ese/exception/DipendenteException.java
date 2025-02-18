package com.example.U4_S7_L2_ese.exception;

import org.springframework.http.HttpStatus;

public class DipendenteException {
    private String message;
    private HttpStatus status;

    public DipendenteException(String msg, HttpStatus stato){
        message = msg;
        status = stato;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
