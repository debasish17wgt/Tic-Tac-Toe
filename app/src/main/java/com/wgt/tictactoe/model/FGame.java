package com.wgt.tictactoe.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class FGame {

    private String player1, player2;
    private String currentPlayer;
    private String winner;
    private boolean accepted;
    private Map<String, String> cell;

    public FGame() {
    }

    public FGame(String player1, String player2, String currentPlayer, String winner, boolean accepted, Map<String, String> cell) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.accepted = accepted;
        this.cell = cell;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
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
