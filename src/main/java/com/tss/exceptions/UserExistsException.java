package com.tss.exceptions;

public class UserExistsException extends RuntimeException {

    public UserExistsException() {
        super("User with given username already exists.");
    }
}
