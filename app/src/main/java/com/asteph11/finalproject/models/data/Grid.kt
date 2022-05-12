package com.asteph11.finalproject.models.data

import com.asteph11.finalproject.models.data.Grid.GridCoord

class Grid {
    enum class Status {
        EMPTY, SHIP, MISS, HIT
    }

    val gridCoords = Array(DIMENSIONS) { arrayOfNulls<GridCoord>(DIMENSIONS) }
    fun setStatus(coordX: Int, coordY: Int, status: Status) {
        gridCoords[coordY][coordX]!!.status = status.ordinal
    }

    fun getStatusIndex(coordX: Int, coordY: Int): Int {
        return gridCoords[coordY][coordX]!!.status
    }

    fun getStatusAt(coordX: Int, coordY: Int): Status {
        return Status.values()[getStatusIndex(coordX, coordY)]
    }

    fun getStatusAt(coordX: Int, coordY: Int, status: Status): Boolean {
        return getStatusAt(coordX, coordY) == status
    }

    override fun toString(): String {
        var t = "Current grid\n"
        for (gc in gridCoords) {
            t += "| "
            for (g in gc) {
                t += String.format("%6s", g) + " | "
            }
            t += "\n"
        }
        return t
    }

    class GridCoord {
        var status = Status.EMPTY.ordinal
        override fun toString(): String {
            return " " + Status.values()[status]
        }
    }

    companion object {
        const val DIMENSIONS = 7
        const val MAX_NUM_SHIPS = (DIMENSIONS * DIMENSIONS * (17 / 100.0)).toInt()
    }

    init {
        for (i in 0 until DIMENSIONS) {
            for (j in 0 until DIMENSIONS) {
                gridCoords[i][j] = GridCoord()
            }
        }
    }
}