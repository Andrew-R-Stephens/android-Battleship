package com.asteph11.finalproject.models.data

import java.lang.Exception
import kotlin.Throws

class Player(var name: String) {
    var grid: Grid? = null
        private set
    private var numShips = 0
    var hits = 0
        private set
    var misses = 0
        private set

    fun setShip(x: Int, y: Int): Boolean {
        if (grid == null) {
            grid = Grid()
        }
        return if (grid!!.getStatusAt(x, y) != Grid.Status.SHIP) {
            if (!isGridReady) {
                grid!!.setStatus(x, y, Grid.Status.SHIP)
                numShips++
                true
            } else {
                false
            }
        } else {
            grid!!.setStatus(x, y, Grid.Status.EMPTY)
            numShips--
            if (numShips < 0) {
                numShips = 0
            }
            true
        }
    }

    /**
     * Sets the state of the player's own grid upon receiving fire
     * @param x horizontal coord
     * @param y vertical coord
     * @return -1 already hit/retry, 0 empty no hit, 1 ship hit
     */
    @Throws(Exception::class)
    fun receiveFire(x: Int, y: Int): Int {
        if (grid == null) {
            throw Exception("Player grid is null!")
        }
        var value = 0
        val status = grid!!.getStatusAt(x, y)
        value = when (status) {
            Grid.Status.HIT, Grid.Status.MISS -> {
                -1
            }
            Grid.Status.SHIP -> {
                grid!!.setStatus(x, y, Grid.Status.HIT)
                1
            }
            Grid.Status.EMPTY -> {
                grid!!.setStatus(x, y, Grid.Status.MISS)
                0
            }
        }
        return value
    }

    val isReady: Boolean
        get() = grid != null
    val isDefeated: Boolean
        get() {
            var isDefeated = true
            for (y in 0 until Grid.DIMENSIONS) {
                for (x in 0 until Grid.DIMENSIONS) {
                    if (grid!!.getStatusAt(x, y, Grid.Status.SHIP)) {
                        isDefeated = false
                    }
                }
            }
            return isDefeated
        }
    val isGridReady: Boolean
        get() = numShips >= Grid.MAX_NUM_SHIPS

    fun addHit() {
        hits++
    }

    fun addMiss() {
        misses++
    }
}