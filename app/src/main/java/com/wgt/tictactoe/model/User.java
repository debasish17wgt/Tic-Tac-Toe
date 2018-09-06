package com.wgt.tictactoe.model;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.firebase.database.Exclude;

import java.util.regex.Pattern;

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

    @Exclude
    public boolean isValid() {
        return name != null && email != null && fdbKey != null;
    }

    @Exclude
    public boolean isValidForLogin() {
        return name != null
                && email != null
                && name.length() >= 3
                && email.length() >= 5;
    }
}
