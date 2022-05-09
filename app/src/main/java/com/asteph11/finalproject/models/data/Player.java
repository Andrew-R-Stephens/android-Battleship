package com.asteph11.finalproject.models.data;

public class Player {

    private String name;
    private Grid grid;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean setShip(int x, int y) {
        if(grid == null) {
            grid = new Grid();
        }

        if(grid.getStatusAt(x, y) != Grid.Status.SHIP) {
            grid.setStatus(x, y, Grid.Status.SHIP);
            return true;
        }

        return false;
    }

    /**
     * Sets the state of the player's own grid upon receiving fire
     * @param x horizontal coord
     * @param y vertical coord
     * @return -1 already hit/retry, 0 empty no hit, 1 ship hit
     */
    public int receiveFire(int x, int y) throws Exception {
        if(grid == null) {
            throw new Exception("Player grid is null!");
        }

        int value = 0;

        Grid.Status status = grid.getStatusAt(x, y);
        switch(status) {
            case HIT: case MISS: {
                value = -1;
                break;
            }
            case SHIP: {
                grid.setStatus(x, y, Grid.Status.HIT);
                value = 1;
                break;
            }
            case EMPTY: {
                grid.setStatus(x, y, Grid.Status.MISS);
                value = 0;
                break;
            }
        }

        return value;
    }

    public Grid getGridObfuscated() throws Exception {
        if(grid == null) {
            throw new Exception("Player grid is null!");
        }

        Grid grid = new Grid();

        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                Grid.Status status = this.grid.getStatusAt(x, y);
                if(status == Grid.Status.SHIP) {
                    grid.setStatus(x, y, Grid.Status.MISS);
                } else {
                    grid.setStatus(x, y, status);
                }

            }
        }

        return grid;
    }

    public boolean isReady() {
        return grid != null;
    }

    public Grid getGrid() {
        return grid;
    }
}