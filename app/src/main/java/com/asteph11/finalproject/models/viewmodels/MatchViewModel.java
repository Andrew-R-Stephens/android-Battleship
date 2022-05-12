package com.asteph11.finalproject.models.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.asteph11.finalproject.models.data.Player;
import com.asteph11.finalproject.models.data.TurnHandler;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchViewModel extends ViewModel {

    private ArrayList<String> topnames;
    private FirebaseFirestore db;
    private TurnHandler turnHandler;

    public void init(FirebaseFirestore db) {
        this.db = db;
        if(turnHandler == null) {
            initMatch(new Player("NA"), new Player("NA"));
        }
        if(topnames == null) {
            setTopList();
        }
    }

    public void initMatch(Player p1, Player p2) {
        turnHandler = new TurnHandler(p1, p2);
    }

    public Player getP1() {
        return turnHandler.getP1();
    }

    public Player getP2() {
        return turnHandler.getP2();
    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public boolean playersReady() throws Exception {
        if(turnHandler == null) {
            throw new Exception("TurnHandler is null!");
        }
        return turnHandler.isReady();
    }

    public boolean isGameFinished() throws Exception {
        return turnHandler.isFinished();
    }

    public void addWinner() {
        db.collection("Winners")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, String> user = new HashMap<>();
                        user.put("name", turnHandler.getCurrentPlayer().getName());
                        user.put("timestamp", ""+System.currentTimeMillis());
                        db.collection("Winners")
                                .document(turnHandler.getCurrentPlayer().getName()).set(user);
                    } else {
                        Log.w("FBDB", "Error getting documents.", task.getException());
                    }
                });
    }

    public ArrayList<String> setTopList() {
        db.collection("Winners").orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(topnames == null) {
                            topnames = new ArrayList<>();
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getData().get("name") + "";
                            topnames.add(name);
                            Log.d("Names", topnames.get(topnames.size()-1));
                        }
                    } else {
                        Log.w("FBDB", "Error getting documents.", task.getException());
                    }
                });

        return topnames;
    }

    public ArrayList<String> getTopList() {
        return topnames;
    }


    public void reset() {
        turnHandler = null;
    }
}