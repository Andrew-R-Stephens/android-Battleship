package com.asteph11.finalproject.controllers.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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

    private View localGrid;
    private ImageView shipanim;
    private ImageView wateranim;

    private boolean canPlace = true;

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
        shipanim = view.findViewById(R.id.animship);
        wateranim = view.findViewById(R.id.animwater);

        TextView turnTitle = view.findViewById(R.id.turnTitle);
        turnTitle.setText(matchViewModel.getTurnHandler().getCurrentPlayer().getName() + "'s Turn");

        createGrid(view);
        setCoordinateListeners();
    }

    protected void createGrid(View fragmentView) {

        gridCoords = new View[Grid.DIMENSIONS][Grid.DIMENSIONS];

        String[] markers = {"A","B","C","D","E","F","G"};
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //+--- Master Grid
        masterGrid = inflater.inflate(
                R.layout.layout_grid,
                (ViewGroup) fragmentView,
                false);
        LinearLayoutCompat grid_linearLayout = masterGrid.findViewById(R.id.gridContainer);
        //---+
        //+--- Personal Grid
        localGrid = inflater.inflate(
                R.layout.layout_grid,
                (ViewGroup) fragmentView,
                false);
        LinearLayoutCompat grid_personal_linearLayout = localGrid.findViewById(R.id.gridContainer);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int localCoordSize = (int)((display.getWidth()*.5f)/((double)Grid.DIMENSIONS+1));
        //---+

        // Set top markers
        View topRowInflater = inflater.inflate(
                R.layout.layout_gridrow,
                (ViewGroup) fragmentView,
                false);
        //+--- Master Grid
        LinearLayoutCompat topRow_linearLayout = topRowInflater.findViewById(R.id.gridrowContainer);
        //---+
        //+--- Personal Grid
        View topRow_personal_Inflater = inflater.inflate(
                R.layout.layout_gridrow,
                (ViewGroup) fragmentView,
                false);
        LinearLayoutCompat topRow_personal_linearLayout = topRow_personal_Inflater.findViewById(R.id.gridrowContainer);
        //---+
        for(int x = 0; x < Grid.DIMENSIONS+1; x++) {
            //+--- Master Grid
            View marker_top = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            marker_top.setLayoutParams(new LinearLayout.LayoutParams(coordSize,
                    coordSize, 1f));
            TextView label = marker_top.findViewById(R.id.label);
            label.setText(x > 0 ? markers[x-1] : "");
            marker_top.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_top.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            topRow_linearLayout.addView(marker_top);
            //---+

            //+--- Personal Grid
            View marker_personal_top = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            marker_personal_top.setLayoutParams(new LinearLayout.LayoutParams(localCoordSize,
                    localCoordSize, 1f));
            TextView label_personal = marker_personal_top.findViewById(R.id.label);
            label_personal.setText(x > 0 ? markers[x-1] : "");
            marker_personal_top.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_personal_top.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            topRow_personal_linearLayout.addView(marker_personal_top);
            //---+
        }

        //+--- Master Grid
        grid_linearLayout.addView(topRowInflater);
        //---+
        //+--- Personal Grid
        grid_personal_linearLayout.addView(topRow_personal_Inflater);
        //---+

        //+--- Master Grid
        gridRows = new View[Grid.DIMENSIONS];
        //---+

        //+--- Personal Grid
        View[] personal_gridRows = new View[Grid.DIMENSIONS];
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            //+--- Master Grid
            gridRows[y] = inflater.inflate(
                    R.layout.layout_gridrow,
                    (ViewGroup) fragmentView,
                    false);
            LinearLayoutCompat row_linearLayout = gridRows[y].findViewById(R.id.gridrowContainer);
            //---+
            //+--- Personal Grid
            personal_gridRows[y] = inflater.inflate(
                    R.layout.layout_gridrow,
                    (ViewGroup) fragmentView,
                    false);
            LinearLayoutCompat row_personal_linearLayout = personal_gridRows[y].findViewById(R.id.gridrowContainer);
            //---+

            // Set left markers
            //+--- Master Grid
            View marker_left = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            marker_left.setLayoutParams(new LinearLayout.LayoutParams(coordSize,
                    coordSize, 1f));
            TextView label = marker_left.findViewById(R.id.label);
            label.setText((y+1)+"");
            marker_left.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_left.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            row_linearLayout.addView(marker_left);
            //---+

            //+--- Personal Grid
            View marker_left_personal = inflater.inflate(
                    R.layout.layout_gridcoord,
                    (ViewGroup) fragmentView,
                    false);
            marker_left_personal.setLayoutParams(new LinearLayout.LayoutParams(localCoordSize,
                    localCoordSize, 1f));
            TextView label_personal = marker_left_personal.findViewById(R.id.label);
            label_personal.setText((y+1)+"");
            marker_left_personal.findViewById(R.id.status).setVisibility(View.INVISIBLE);
            marker_left_personal.findViewById(R.id.water).setVisibility(View.INVISIBLE);
            row_personal_linearLayout.addView(marker_left_personal);
            //---+

            // Add columns
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                //+--- Master Grid
                gridCoords[y][x] = inflater.inflate(
                        R.layout.layout_gridcoord,
                        (ViewGroup) fragmentView,
                        false);
                gridCoords[y][x].setLayoutParams(new LinearLayout.LayoutParams(coordSize,
                        coordSize, 1f));
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
                //---+

                //+--- Personal Grid
                View coord_personal = inflater.inflate(
                        R.layout.layout_gridcoord,
                        (ViewGroup) fragmentView,
                        false);
                coord_personal.setLayoutParams(new LinearLayout.LayoutParams(localCoordSize,
                        localCoordSize, 1f));
                ImageView status_personal = coord_personal.findViewById(R.id.status);
                switch(matchViewModel.getTurnHandler().getCurrentPlayer().getGrid().getStatusAt(x, y)) {
                    case SHIP: {
                        status_personal.setVisibility(View.VISIBLE);
                        status_personal.setImageDrawable(getResources().getDrawable(R.drawable.ship,
                                getActivity().getTheme()));
                        break;
                    }
                    case MISS: {
                        status_personal.setVisibility(View.VISIBLE);
                        status_personal.setImageDrawable(getResources().getDrawable(R.drawable.miss, getActivity().getTheme()));
                        break;
                    }
                    case HIT: {
                        status_personal.setVisibility(View.VISIBLE);
                        status_personal.setImageDrawable(getResources().getDrawable(R.drawable.hit, getActivity().getTheme()));
                        break;
                    }
                    default : {
                        status_personal.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
                row_personal_linearLayout.addView(coord_personal);
                //---+
            }
            grid_linearLayout.addView(gridRows[y]);
            grid_personal_linearLayout.addView(personal_gridRows[y]);
        }

        ConstraintLayout gridContainer = fragmentView.findViewById(R.id.gridLayout);
        gridContainer.addView(masterGrid);

        ConstraintLayout gridContainer_personal = fragmentView.findViewById(R.id.myGridLayout);
        gridContainer_personal.addView(localGrid);
    }

    protected void setCoordinateListeners() {
        for(int y = 0; y < Grid.DIMENSIONS; y++) {
            for(int x = 0; x < Grid.DIMENSIONS; x++) {
                int finalX = x;
                int finalY = y;
                gridCoords[y][x].setOnClickListener(view -> {
                    if(!canPlace) {
                        return;
                    }

                    int status = 0;
                    try {
                         status =
                                matchViewModel.getTurnHandler().getOtherPlayer().receiveFire(finalX, finalY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(status == -1) {
                        return;
                    } else {
                        canPlace = false;
                    }

                    boolean gameFinished = false;
                    try {
                        gameFinished = matchViewModel.getTurnHandler().isFinished();
                    } catch (Exception e) {
                        Log.e("Swap", e.getMessage());
                    }

                    boolean finalGameFinished = gameFinished;
                    if (status == 0) {
                        matchViewModel.getTurnHandler().getCurrentPlayer().addMiss();

                        ImageView imagestatus =
                                gridCoords[finalY][finalX].findViewById(R.id.status);
                        imagestatus.setBackground(getResources().getDrawable(R.drawable.miss,
                                getActivity().getTheme()));
                        imagestatus.setAlpha(0f);
                        imagestatus.setVisibility(View.VISIBLE);
                        imagestatus.animate().alpha(1f).setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                        if(!finalGameFinished) {
                                            matchViewModel.getTurnHandler().swapTurns();
                                            Navigation.findNavController(view).
                                                    navigate(R.id.action_gameFragment_to_awaitSwapFragment);
                                        } else {
                                            matchViewModel.addWinner();
                                            Navigation.findNavController(view).
                                                    navigate(R.id.action_gameFragment_to_scoreFragment);
                                        }
                                    }
                                }).start();
                    } else {
                        matchViewModel.getTurnHandler().getCurrentPlayer().addHit();

                        wateranim.setAlpha(1f);
                        wateranim.setVisibility(View.VISIBLE);
                        wateranim.animate().setDuration(1500).start();

                        RotateAnimation rotate = new RotateAnimation(
                                0, -120, Animation.RELATIVE_TO_SELF,
                                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(5000);
                        rotate.setInterpolator(new LinearInterpolator());

                        shipanim.setAnimation(rotate);
                        shipanim.setAlpha(1f);
                        shipanim.setVisibility(View.VISIBLE);
                        shipanim.animate()
                                .setDuration(1500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                        if(!finalGameFinished) {
                                            matchViewModel.getTurnHandler().swapTurns();
                                            Navigation.findNavController(view).
                                                    navigate(R.id.action_gameFragment_to_awaitSwapFragment);
                                        } else {
                                            matchViewModel.addWinner();
                                            Navigation.findNavController(view).
                                                    navigate(R.id.action_gameFragment_to_scoreFragment);
                                        }
                                    }
                                }).start();
                    }
/*

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
                        matchViewModel.addWinner();
                        Navigation.findNavController(view).
                                navigate(R.id.action_gameFragment_to_scoreFragment);
                    }
*/

                });
            }
        }
    }
}