package com.asteph11.finalproject.models.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.asteph11.finalproject.models.data.Player
import com.asteph11.finalproject.models.data.TurnHandler
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class MatchViewModel : ViewModel() {
    var topList: ArrayList<String>? = null
        private set
    private var db: FirebaseFirestore? = null
    var turnHandler: TurnHandler? = null
        private set

    fun init(db: FirebaseFirestore?) {
        this.db = db
        if (turnHandler == null) {
            initMatch(Player("NA"), Player("NA"))
        }
        if (topList == null) {
            setTopList()
        }
    }

    fun initMatch(p1: Player?, p2: Player?) {
        turnHandler = TurnHandler(p1, p2)
    }

    val p1: Player?
        get() = turnHandler!!.p1
    val p2: Player?
        get() = turnHandler!!.p2

    @Throws(Exception::class)
    fun playersReady(): Boolean {
        if (turnHandler == null) {
            throw Exception("TurnHandler is null!")
        }
        return turnHandler!!.isReady
    }

    fun addWinner() {
        db!!.collection("Winners")
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot?> ->
                if (task.isSuccessful) {
                    val user: MutableMap<String, String> = HashMap()
                    user["name"] = turnHandler!!.currentPlayer!!.name
                    user["timestamp"] = "" + System.currentTimeMillis()
                    db!!.collection("Winners")
                        .document(turnHandler!!.currentPlayer!!.name).set(user)
                } else {
                    Log.w("FBDB", "Error getting documents.", task.exception)
                }
            }
    }

    fun setTopList(): ArrayList<String>? {
        db!!.collection("Winners").orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    if (topList == null) {
                        topList = ArrayList()
                    }
                    for (document in task.result) {
                        val name = document.data["name"].toString() + ""
                        topList!!.add(name)
                        Log.d("Names", topList!![topList!!.size - 1])
                    }
                } else {
                    Log.w("FBDB", "Error getting documents.", task.exception)
                }
            }
        return topList
    }

    fun reset() {
        turnHandler = null
    }
}