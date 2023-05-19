package com.tss.exceptions;

public class EntityByNameNotFoundException extends RuntimeException {
    public EntityByNameNotFoundException(String entity,String name) {
        super("Could not find " + entity + " with a name " + name);
    }
}
