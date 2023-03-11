package com.betting.javabettingapi.exception;

public class InvalidPlayerException extends RuntimeException{
    public InvalidPlayerException(String message) {
        super(message);
    }
}
