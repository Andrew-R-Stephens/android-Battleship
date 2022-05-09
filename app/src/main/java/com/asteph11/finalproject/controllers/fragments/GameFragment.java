package com.asteph11.finalproject.controllers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.data.Grid;

public class GameFragment extends AGridFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return super.getLayoutInflater().inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createGrid(view);
        setCoordinateListeners();
    }

    protected void createGrid(View fragmentView) {
        gridCoords = new View[Grid.DIMENSIONS][Grid.DIMENSIONS];

        String[] markers = {"A","B","C","D","E","F","G"};

        LayoutInflater inflater = LayoutInflater.from(getContext());
        masterGrid = inflater.inflate(
                R.layout.layout_grid,
                (ViewGroup) fragmentView,
                false);
        LinearLayoutCompat grid_linearLayout = masterGrid.findViewById(R.id.gridContainer);

        // Set top markers
        View topRowInflater = inflater.inflate(
                R.layout.layout_gridrow,
                (ViewGroup) fragmentView,
                false);
        LinearLayoutCompat topRow_linearLayout = topRowInflater.findViewById(R.id.gridrowContainer);
        for(int x = 0; x < Grid.DIMENSIONS+1; x++) {
            View marker_top = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            TextView label = marker_top.findViewById(R.id.label);
            label.setText(x > 0 ? markers[x-1] : "");
            marker_top.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_top.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            topRow_linearLayout.addView(marker_top);
            Log.d("Cycle", x+"");
        }
        Log.d("Parent", topRow_linearLayout.getParent().toString());
        grid_linearLayout.addView(topRowInflater);

        gridRows = new View[Grid.DIMENSIONS];
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            gridRows[y] = inflater.inflate(
                    R.layout.layout_gridrow,
                    (ViewGroup) fragmentView,
                    false);
            LinearLayoutCompat row_linearLayout = gridRows[y].findViewById(R.id.gridrowContainer);

            // Set left markers
            View marker_left = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            TextView label = marker_left.findViewById(R.id.label);
            label.setText((y+1)+"");
            marker_left.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_left.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            row_linearLayout.addView(marker_left);

            // Add columns
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                gridCoords[y][x] = inflater.inflate(
                        R.layout.layout_gridcoord,
                        (ViewGroup) fragmentView,
                        false);
                ImageView status = gridCoords[y][x].findViewById(R.id.status);
                switch(matchViewModel.getTurnHandler().getOtherPlayer().getGrid().getStatusAt(x, y)) {
                    case MISS: {
                        status.setVisibility(View.VISIBLE);
                        status.setImageDrawable(getResources().getDrawable(R.drawable.miss, getActivity().getTheme()));
                        break;
                    }
                    case HIT: {
                        status.setVisibility(View.VISIBLE);
                        status.setImageDrawable(getResources().getDrawable(R.drawable.hit, getActivity().getTheme()));
                        break;
                    }
                    default : {
                        status.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                row_linearLayout.addView(gridCoords[y][x]);
            }
            grid_linearLayout.addView(gridRows[y]);
        }

        ConstraintLayout gridContainer = fragmentView.findViewById(R.id.gridLayout);
        gridContainer.addView(masterGrid);
    }

    protected void setCoordinateListeners() {
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                int finalX = x;
                int finalY = y;
                gridCoords[y][x].setOnClickListener(view -> {

                    try {
                        matchViewModel.getTurnHandler().getOtherPlayer().receiveFire(finalX, finalY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    boolean gameFinished = false;
                    try {
                        gameFinished = matchViewModel.isGameFinished();
                    } catch (Exception e) {
                        Log.e("Swap", e.getMessage());
                    }
                    if(!gameFinished) {
                        matchViewModel.getTurnHandler().swapTurns();
                        Navigation.findNavController(view).
                                navigate(R.id.action_gameFragment_to_awaitSwapFragment);
                    } else {
                        Navigation.findNavController(view).
                                navigate(R.id.action_gameFragment_to_scoreFragment);
                    }

                });
            }
        }
    }
}