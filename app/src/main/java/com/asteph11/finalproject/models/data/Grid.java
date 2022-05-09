package com.asteph11.finalproject.models.data;

public class Grid {

    public static final int DIMENSIONS = 7;

    public enum Status {
        EMPTY,
        SHIP,
        MISS,
        HIT
    }

    private final GridCoord[][] grid = new GridCoord[DIMENSIONS][DIMENSIONS];

    public Grid() {
        for(int i = 0; i < DIMENSIONS; i++) {
            for(int j = 0; j < DIMENSIONS; j++) {
                grid[i][j] = new GridCoord();
            }
        }
    }

    public void setStatus(int coordX, int coordY, Status status) {
        grid[coordY][coordX].setStatus(status.ordinal());
    }

    public int getStatusIndex(int coordX, int coordY) {
        return grid[coordY][coordX].getStatus();
    }

    public Status getStatusAt(int coordX, int coordY) {
        return Status.values()[getStatusIndex(coordX, coordY)];
    }

    public boolean getStatusAt(int coordX, int coordY, Status status) {
        return getStatusAt(coordX, coordY) == status;
    }

    public GridCoord[][] getGridCoords() {
        return grid;
    }

    public String toString() {
        String t = "Current grid\n";

        for(GridCoord[] gc : grid) {
            t += "| ";
            for(GridCoord g: gc) {
                t += String.format("%6s", g) + " | ";
            }
            t += "\n";
        }

        return t;
    }

    static class GridCoord {

        private int status = Grid.Status.EMPTY.ordinal();

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String toString() {
            return " " + Grid.Status.values()[status];
        }

    }
}
