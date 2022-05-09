package com.asteph11.finalproject.models.data;

public class TurnHandler {

    int current = 1;
    private Player[] players;

    public TurnHandler(Player p1, Player p2) {
        this.players = new Player[]{p1, p2};
    }

    private int swapIndex() {
        return current = ((current+1) % 2);
    }

    public Player swapTurns() {
        swapIndex();

        return players[current];
    }

    public Player getCurrentPlayer() {
        return players[current];
    }

    public Player getP1() {
        return players[0];
    }

    public Player getP2() {
        return players[1];
    }

    public String toString() {
        return "Current Player: [" + current + "] " + players[current];
    }

}