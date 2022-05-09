package com.asteph11.finalproject.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.controllers.activities.GameActivity;
import com.asteph11.finalproject.models.viewmodels.MatchViewModel;

public class MainFragment extends Fragment {

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
        }
        // INITIALIZE VIEW MODEL
        if (getContext() != null) {
            matchViewModel.init(getContext());
        }

        return inflater.inflate(R.layout.fragment_startscreen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CardView startButton = view.findViewById(R.id.button_start);
        startButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    startActivity(intent);
                }
        );
    }

}