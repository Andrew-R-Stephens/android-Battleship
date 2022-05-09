package com.asteph11.finalproject.controllers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.data.Grid;

public class SetupFragment extends AGridFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button readyButton = view.findViewById(R.id.readyButton);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        masterGrid = inflater.inflate(
                R.layout.layout_grid,
                (ViewGroup) view,
                false);
        createGrid(view, masterGrid);
        setCoordinateListeners();

        readyButton.setOnClickListener(v -> {
            boolean gameReady = false;
            try {
                gameReady = matchViewModel.playersReady();
            } catch (Exception e) {
                Log.e("Swap", e.getMessage());
            }
            matchViewModel.getTurnHandler().swapTurns();
            if(!gameReady) {
                Navigation.findNavController(v).
                        navigate(R.id.action_setupFragment_to_awaitSwapFragment);
            } else {
                Navigation.findNavController(v).
                        navigate(R.id.action_setupFragment_to_gameFragment);
            }

        });
    }

    protected void setCoordinateListeners() {
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                int finalX = x;
                int finalY = y;
                gridCoords[y][x].setOnClickListener(view -> {
                    matchViewModel.getTurnHandler().getCurrentPlayer().setShip(finalX, finalY);
                    ImageView status = gridCoords[finalY][finalX].findViewById(R.id.status);
                    status.setImageDrawable(getResources().getDrawable(R.drawable.ship, getActivity().getTheme()));
                    status.setVisibility(View.VISIBLE);
                    try {
                        System.out.println(matchViewModel.getTurnHandler().getCurrentPlayer().getGrid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}