package com.asteph11.finalproject.controllers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.viewmodels.MatchViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ScoreFragment extends Fragment {

    private MatchViewModel matchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // OBTAIN VIEW MODEL REFERENCE
        if (matchViewModel == null) {
            matchViewModel = new ViewModelProvider(requireActivity()).
                    get(MatchViewModel.class);
            // INITIALIZE VIEW MODEL
            matchViewModel.init(FirebaseFirestore.getInstance());
        }

        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView p1Name = view.findViewById(R.id.player1title);
        TextView p2Name = view.findViewById(R.id.player2title);
        TextView p1Hits = view.findViewById(R.id.p1shipshittotal);
        TextView p2Hits = view.findViewById(R.id.p2shipshittotal);
        TextView p1Miss = view.findViewById(R.id.p1shipsmissedtotal);
        TextView p2Miss = view.findViewById(R.id.p2shipsmissedtotal);
        LinearLayoutCompat linearLayout = view.findViewById(R.id.winnersList);
        CardView rematchButton = view.findViewById(R.id.button_newmatch);

        p1Name.setText("" + matchViewModel.getTurnHandler().getP2().getName());
        p2Name.setText("" + matchViewModel.getTurnHandler().getP1().getName());
        p1Hits.setText("" + matchViewModel.getTurnHandler().getP2().getHits());
        p2Hits.setText("" + matchViewModel.getTurnHandler().getP1().getHits());
        p1Miss.setText("" + matchViewModel.getTurnHandler().getP2().getMisses());
        p2Miss.setText("" + matchViewModel.getTurnHandler().getP1().getMisses());

        ArrayList<String> names = matchViewModel.getTopList();
        for(int i = 0; i < names.size() && i < 3; i++) {
            View layout = getLayoutInflater().inflate(R.layout.layout_winnertextview,
                    (ViewGroup) view, false);
            TextView textView = layout.findViewById(R.id.winnername);
            textView.setText(names.get(i));
            linearLayout.addView(layout);
        }

        rematchButton.setOnClickListener(v -> {
            matchViewModel.reset();
            Navigation.findNavController(v).navigate(R.id.action_scoreFragment_to_setupFragment);
        });

    }

}