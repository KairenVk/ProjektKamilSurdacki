package com.tss.entities;

import com.tss.entities.credentials.Credentials;

public class RestForm extends Credentials {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
