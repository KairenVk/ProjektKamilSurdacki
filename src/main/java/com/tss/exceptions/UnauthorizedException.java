package com.tss.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Missing permission to perform query");
    }
}
