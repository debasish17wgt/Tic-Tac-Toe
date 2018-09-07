package com.wgt.tictactoe.model;


public class Player {

    public String name;
    public String visibleName;
    public String value;

    public Player() {
    }

    public Player(String name, String visibleName, String value) {
        this.name = name;
        this.visibleName = visibleName;
        this.value = value;
    }
}
