package com.asteph11.finalproject.models.data

class TurnHandler(p1: Player?, p2: Player?) {
    var turnCount = 0
        private set
    private var current = 1
    private val players: Array<Player?>?
    private fun swapIndex(): Int {
        current = (current + 1) % 2
        if (current == 1) {
            turnCount++
        }
        return current
    }

    fun swapTurns(): Player? {
        swapIndex()
        return players!![current]
    }

    val currentPlayer: Player?
        get() = players!![current]
    val otherPlayer: Player?
        get() = players!![(current + 1) % 2]
    val p1: Player?
        get() = players!![0]
    val p2: Player?
        get() = players!![1]

    override fun toString(): String {
        return "Current Player: [" + current + "] " + players!![current]
    }

    val isReady: Boolean
        get() = if (players == null || players[0] == null || players[1] == null) {
            false
        } else p1!!.isReady && p2!!.isReady
    val isFinished: Boolean
        get() {
            isReady
            return p1!!.isDefeated || p2!!.isDefeated
        }

    init {
        players = arrayOf(p1, p2)
    }
}