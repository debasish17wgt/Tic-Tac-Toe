package com.wgt.tictactoe.model;

import com.google.firebase.database.Exclude;

public class User {
    private String name, email, fdbKey;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String fdbKey) {
        this.name = name;
        this.email = email;
        this.fdbKey = fdbKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Exclude
    public String getFdbKey() {
        return fdbKey;
    }

    @Exclude
    public void setFdbKey(String fdbKey) {
        this.fdbKey = fdbKey;
    }
}
