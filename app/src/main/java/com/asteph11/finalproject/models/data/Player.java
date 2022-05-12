package com.asteph11.finalproject.models.data;

public class Player {

    private String name;
    private Grid grid;

    private int numShips = 0;
    private int hits, misses;

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
            if(!isGridReady()) {
                grid.setStatus(x, y, Grid.Status.SHIP);
                numShips++;
                return true;
            } else {
                return false;
            }
        } else {
            grid.setStatus(x, y, Grid.Status.EMPTY);
            numShips--;
            if(numShips < 0) {
                numShips = 0;
            }
            return true;
        }
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

    public boolean isReady() {
        return grid != null;
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean isDefeated() {
        boolean isDefeated = true;
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                if(grid.getStatusAt(x, y, Grid.Status.SHIP)) {
                    isDefeated = false;
                }
            }
        }
        return isDefeated;
    }

    public void setName(String nameInput) {
        this.name = nameInput;
    }

    public boolean isGridReady() {
        return numShips >= Grid.MAX_NUM_SHIPS;
    }

    public void addHit() {
        hits++;
    }

    public void addMiss() {
        misses++;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }
}
