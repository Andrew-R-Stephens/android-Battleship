package com.asteph11.finalproject.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.viewmodels.MatchViewModel;

public class AwaitSwapFragment extends Fragment {

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

        return inflater.inflate(R.layout.fragment_awaitswap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView request = view.findViewById(R.id.text_request);


        if(!matchViewModel.getTurnHandler().getCurrentPlayer().isReady()) {
            request.setText("Please hand device over to other player");
        } else {
            request.setText("Please hand device over to " + matchViewModel.getTurnHandler().getCurrentPlayer().getName());
        }

        CardView swappedButton = view.findViewById(R.id.button_swapped);
        swappedButton.setOnClickListener(v -> {
            boolean isGameReady = false;
            try {
                isGameReady = matchViewModel.playersReady();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!isGameReady) {
                Navigation.findNavController(v).popBackStack();
            } else {
                Navigation.findNavController(v).
                        navigate(R.id.action_awaitSwapFragment_to_gameFragment);
            }
        });
    }

}