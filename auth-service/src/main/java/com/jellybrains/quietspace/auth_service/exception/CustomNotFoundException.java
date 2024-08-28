package com.jellybrains.quietspace.auth_service.exception;


public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(){
        super("resource not found");
    }
    public CustomNotFoundException(String message) {
        super(message);
    }
}
