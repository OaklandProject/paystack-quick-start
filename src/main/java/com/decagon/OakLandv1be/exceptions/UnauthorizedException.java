package com.decagon.OakLandv1be.exceptions;

public class UnauthorizedException extends RuntimeException{
    private String debugMsg;
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String debugMsg) {
        super(message);
        this.debugMsg = debugMsg;
    }
}
