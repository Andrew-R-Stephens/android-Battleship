package com.asteph11.finalproject.controllers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.data.Grid;
import com.asteph11.finalproject.models.viewmodels.MatchViewModel;

public class SetupFragment extends Fragment {

    private MatchViewModel matchViewModel;

    private View masterGrid;
    private View[] gridRows;
    private View[][] gridCoords;

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
            matchViewModel.init(getContext());
        }

        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button readyButton = view.findViewById(R.id.readyButton);

        createGrid(view);
        setCoordinateListeners();

        readyButton.setOnClickListener(v -> {
            boolean gameReady = false;
            try {
                gameReady = matchViewModel.gameReady();
            } catch (Exception e) {
                Log.e("Swap", e.getMessage());
            }
            if(!gameReady) {
                matchViewModel.getTurnHandler().swapTurns();
                Navigation.findNavController(v).
                        navigate(R.id.action_setupFragment_to_awaitSwapFragment);
            } else {
                Navigation.findNavController(v).
                        navigate(R.id.action_setupFragment_to_gameFragment);
            }

        });
    }

    private void createGrid(View view) {
        gridCoords = new View[Grid.DIMENSIONS][Grid.DIMENSIONS];

        String[] markers = {"A","B","C","D","E","F","G"};

        LayoutInflater inflater = LayoutInflater.from(getContext());
        masterGrid = inflater.inflate(
                R.layout.layout_grid,
                (ViewGroup) view,
                false);
        LinearLayoutCompat grid_linearLayout = masterGrid.findViewById(R.id.gridContainer);

        // Set top markers
        View topRowInflater = inflater.inflate(
                R.layout.layout_gridrow,
                (ViewGroup) view,
                false);
        LinearLayoutCompat topRow_linearLayout = topRowInflater.findViewById(R.id.gridrowContainer);
        for(int x = 0; x < Grid.DIMENSIONS+1; x++) {
            View marker_top = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) view,
                    false);
            TextView label = marker_top.findViewById(R.id.label);
            label.setText(x > 0 ? markers[x-1] : "");
            ((ImageView)(marker_top.findViewById(R.id.status))).setVisibility(View.INVISIBLE);
            ((ImageView)(marker_top.findViewById(R.id.water))).setVisibility(View.INVISIBLE);
            topRow_linearLayout.addView(marker_top);
            Log.d("Cycle", x+"");
        }
        Log.d("Parent", topRow_linearLayout.getParent().toString());
        grid_linearLayout.addView(topRowInflater);

        gridRows = new View[Grid.DIMENSIONS];
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            gridRows[y] = inflater.inflate(
                    R.layout.layout_gridrow,
                    (ViewGroup) view,
                    false);
            LinearLayoutCompat row_linearLayout = gridRows[y].findViewById(R.id.gridrowContainer);

            // Set left markers
            View marker_left = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) view,
                    false);
            TextView label = marker_left.findViewById(R.id.label);
            label.setText((y+1)+"");
            ((ImageView)(marker_left.findViewById(R.id.status))).setVisibility(View.INVISIBLE);
            ((ImageView)(marker_left.findViewById(R.id.water))).setVisibility(View.INVISIBLE);
            row_linearLayout.addView(marker_left);

            // Add columns
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                gridCoords[y][x] = inflater.inflate(
                        R.layout.layout_gridcoord,
                        (ViewGroup) view,
                        false);
                ((ImageView)(gridCoords[y][x].findViewById(R.id.status))).setVisibility(View.INVISIBLE);
                row_linearLayout.addView(gridCoords[y][x]);
            }
            grid_linearLayout.addView(gridRows[y]);
        }

        ConstraintLayout gridContainer = view.findViewById(R.id.gridLayout);
        gridContainer.addView(masterGrid);
    }

    private void setCoordinateListeners() {
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                int finalX = x;
                int finalY = y;
                gridCoords[y][x].setOnClickListener(view -> {
                    matchViewModel.getTurnHandler().getCurrentPlayer().setShip(finalX, finalY);
                    ((ImageView)(gridCoords[finalY][finalX].findViewById(R.id.status))).setVisibility(View.VISIBLE);
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