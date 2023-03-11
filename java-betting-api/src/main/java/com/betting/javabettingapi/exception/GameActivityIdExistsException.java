package com.betting.javabettingapi.exception;

public class GameActivityIdExistsException extends RuntimeException{
    public GameActivityIdExistsException(String message) {
        super(message);
    }
}
