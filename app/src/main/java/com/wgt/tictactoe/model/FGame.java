package com.wgt.tictactoe.model;

import android.support.annotation.Keep;

import com.google.firebase.database.Exclude;

import java.util.Map;

@Keep
public class FGame {

    private User player1, player2;
    private String currentPlayer;
    private Player winner;
    private boolean accepted;
    private Map<String, String> cell;

    public FGame() {
    }

    public FGame(User player1, User player2, String currentPlayer, Player winner, boolean accepted, Map<String, String> cell) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.accepted = accepted;
        this.cell = cell;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Map<String, String> getCell() {
        return cell;
    }

    public void setCell(Map<String, String> cell) {
        this.cell = cell;
    }
}
