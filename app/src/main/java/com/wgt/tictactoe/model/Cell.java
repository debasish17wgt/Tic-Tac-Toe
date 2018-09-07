package com.wgt.tictactoe.model;

import android.support.annotation.Keep;

import com.wgt.tictactoe.util.StringUtility;

@Keep
public class Cell {

    public Player player;

    public Cell(Player player) {
        this.player = player;
    }

    public boolean isEmpty() {
        return player == null || StringUtility.isNullOrEmpty(player.value);
    }
}
