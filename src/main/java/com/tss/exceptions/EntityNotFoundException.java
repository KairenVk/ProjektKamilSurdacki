package com.tss.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Long id) {
        super("Could not find " + entity + " with an ID " + id);
    }
}
