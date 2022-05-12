package com.asteph11.finalproject.models.data;

public class TurnHandler {

    private int turnCount = 0;

    private int current = 1;
    private Player[] players;

    public TurnHandler(Player p1, Player p2) {
        this.players = new Player[]{p1, p2};
    }

    private int swapIndex() {
        current = ((current+1) % 2);

        if(current == 1) {
            turnCount++;
        }

        return current;
    }

    public Player swapTurns() {
        swapIndex();

        return players[current];
    }

    public int getTurnCount() {
        return turnCount;
    }

    public Player getCurrentPlayer() {
        return players[current];
    }

    public Player getOtherPlayer() {
        return players[((current+1) % 2)];
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

    public boolean isReady() {
        if(players == null || players[0] == null || players[1] == null) {
            return false;
        }

        return getP1().isReady() && getP2().isReady();
    }

    public boolean isFinished() {
        isReady();
        return (getP1().isDefeated() || getP2().isDefeated());
    }
}
