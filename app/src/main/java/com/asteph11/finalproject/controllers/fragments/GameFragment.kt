package com.asteph11.finalproject.controllers.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import com.asteph11.finalproject.R
import com.asteph11.finalproject.models.data.Grid

class GameFragment : AGridFragment() {
    private var localGrid: View? = null
    private var shipanim: ImageView? = null
    private var wateranim: ImageView? = null
    private var canPlace = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return super.getLayoutInflater().inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shipanim = view.findViewById(R.id.animship)
        wateranim = view.findViewById(R.id.animwater)
        val turnTitle = view.findViewById<TextView>(R.id.turnTitle)
        turnTitle.text = matchViewModel?.turnHandler!!.currentPlayer!!.name + "'s Turn"
        createGrid(view)
        setCoordinateListeners()
    }

    protected fun createGrid(fragmentView: View) {
        gridCoords = Array(Grid.DIMENSIONS) { arrayOfNulls(Grid.DIMENSIONS) }
        val markers = arrayOf("A", "B", "C", "D", "E", "F", "G")
        val inflater = requireActivity().layoutInflater

        //+--- Master Grid
        masterGrid = inflater.inflate(
            R.layout.layout_grid,
            fragmentView as ViewGroup,
            false
        )
        val grid_linearLayout = masterGrid?.findViewById<LinearLayoutCompat>(R.id.gridContainer)
        //---+
        //+--- Personal Grid
        localGrid = inflater.inflate(
            R.layout.layout_grid,
            fragmentView,
            false
        )
        val grid_personal_linearLayout =
            localGrid!!.findViewById<LinearLayoutCompat>(R.id.gridContainer)
        val display = requireActivity().windowManager.defaultDisplay
        val localCoordSize = (display.width * .5f / (Grid.DIMENSIONS.toDouble() + 1)).toInt()
        //---+

        // Set top markers
        val topRowInflater = inflater.inflate(
            R.layout.layout_gridrow,
            fragmentView,
            false
        )
        //+--- Master Grid
        val topRow_linearLayout =
            topRowInflater.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)
        //---+
        //+--- Personal Grid
        val topRow_personal_Inflater = inflater.inflate(
            R.layout.layout_gridrow,
            fragmentView,
            false
        )
        val topRow_personal_linearLayout =
            topRow_personal_Inflater.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)
        //---+
        for (x in 0 until Grid.DIMENSIONS + 1) {
            //+--- Master Grid
            val marker_top = inflater.inflate(
                R.layout.layout_gridcoord,
                fragmentView,
                false
            )
            marker_top.layoutParams = LinearLayout.LayoutParams(
                coordSize,
                coordSize, 1f
            )
            val label = marker_top.findViewById<TextView>(R.id.label)
            label.text = if (x > 0) markers[x - 1] else ""
            marker_top.findViewById<View>(R.id.status).visibility = View.INVISIBLE
            marker_top.findViewById<View>(R.id.water).visibility = View.INVISIBLE
            topRow_linearLayout.addView(marker_top)
            //---+

            //+--- Personal Grid
            val marker_personal_top = inflater.inflate(
                R.layout.layout_gridcoord,
                fragmentView,
                false
            )
            marker_personal_top.layoutParams = LinearLayout.LayoutParams(
                localCoordSize,
                localCoordSize, 1f
            )
            val label_personal = marker_personal_top.findViewById<TextView>(R.id.label)
            label_personal.text = if (x > 0) markers[x - 1] else ""
            marker_personal_top.findViewById<View>(R.id.status).visibility = View.INVISIBLE
            marker_personal_top.findViewById<View>(R.id.water).visibility = View.INVISIBLE
            topRow_personal_linearLayout.addView(marker_personal_top)
            //---+
        }

        //+--- Master Grid
        grid_linearLayout?.addView(topRowInflater)
        //---+
        //+--- Personal Grid
        grid_personal_linearLayout.addView(topRow_personal_Inflater)
        //---+

        //+--- Master Grid
        gridRows = arrayOfNulls(Grid.DIMENSIONS)
        //---+

        //+--- Personal Grid
        val personal_gridRows = arrayOfNulls<View>(Grid.DIMENSIONS)
        for (y in 0 until Grid.DIMENSIONS) {
            //+--- Master Grid
            gridRows[y] = inflater.inflate(
                R.layout.layout_gridrow,
                fragmentView,
                false
            )
            val row_linearLayout =
                gridRows[y]?.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)
            //---+
            //+--- Personal Grid
            personal_gridRows[y] = inflater.inflate(
                R.layout.layout_gridrow,
                fragmentView,
                false
            )
            val row_personal_linearLayout =
                personal_gridRows[y]?.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)
            //---+

            // Set left markers
            //+--- Master Grid
            val marker_left = inflater.inflate(
                R.layout.layout_gridcoord,
                fragmentView,
                false
            )
            marker_left.layoutParams = LinearLayout.LayoutParams(
                coordSize,
                coordSize, 1f
            )
            val label = marker_left.findViewById<TextView>(R.id.label)
            label.text = (y + 1).toString() + ""
            marker_left.findViewById<View>(R.id.status).visibility = View.INVISIBLE
            marker_left.findViewById<View>(R.id.water).visibility = View.INVISIBLE
            row_linearLayout?.addView(marker_left)
            //---+

            //+--- Personal Grid
            val marker_left_personal = inflater.inflate(
                R.layout.layout_gridcoord,
                fragmentView,
                false
            )
            marker_left_personal.layoutParams = LinearLayout.LayoutParams(
                localCoordSize,
                localCoordSize, 1f
            )
            val label_personal = marker_left_personal.findViewById<TextView>(R.id.label)
            label_personal.text = (y + 1).toString() + ""
            marker_left_personal.findViewById<View>(R.id.status).visibility =
                View.INVISIBLE
            marker_left_personal.findViewById<View>(R.id.water).visibility =
                View.INVISIBLE
            row_personal_linearLayout?.addView(marker_left_personal)
            //---+

            // Add columns
            for (x in 0 until Grid.DIMENSIONS) {
                //+--- Master Grid
                gridCoords[y][x] = inflater.inflate(
                    R.layout.layout_gridcoord,
                    fragmentView,
                    false
                )
                gridCoords[y][x]?.layoutParams = LinearLayout.LayoutParams(
                    coordSize,
                    coordSize, 1f
                )
                val status = gridCoords[y][x]?.findViewById<ImageView>(R.id.status)
                when (matchViewModel?.turnHandler!!.otherPlayer!!.grid!!.getStatusAt(x, y)) {
                    Grid.Status.MISS -> {
                        status?.visibility = View.VISIBLE
                        status?.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.miss,
                                requireActivity().theme
                            )
                        )
                    }
                    Grid.Status.HIT -> {
                        status?.visibility = View.VISIBLE
                        status?.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.hit,
                                requireActivity().theme
                            )
                        )
                    }
                    else -> {
                        status?.visibility = View.INVISIBLE
                    }
                }
                row_linearLayout?.addView(gridCoords[y][x])
                //---+

                //+--- Personal Grid
                val coord_personal = inflater.inflate(
                    R.layout.layout_gridcoord,
                    fragmentView,
                    false
                )
                coord_personal.layoutParams = LinearLayout.LayoutParams(
                    localCoordSize,
                    localCoordSize, 1f
                )
                val status_personal = coord_personal.findViewById<ImageView>(R.id.status)
                when (matchViewModel?.turnHandler!!.currentPlayer!!.grid!!.getStatusAt(x, y)) {
                    Grid.Status.SHIP -> {
                        status_personal.visibility = View.VISIBLE
                        status_personal.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ship,
                                requireActivity().theme
                            )
                        )
                    }
                    Grid.Status.MISS -> {
                        status_personal.visibility = View.VISIBLE
                        status_personal.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.miss,
                                requireActivity().theme
                            )
                        )
                    }
                    Grid.Status.HIT -> {
                        status_personal.visibility = View.VISIBLE
                        status_personal.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.hit,
                                requireActivity().theme
                            )
                        )
                    }
                    else -> {
                        status_personal.visibility = View.INVISIBLE
                    }
                }
                row_personal_linearLayout?.addView(coord_personal)
                //---+
            }
            grid_linearLayout?.addView(gridRows[y])
            grid_personal_linearLayout.addView(personal_gridRows[y])
        }
        val gridContainer = fragmentView.findViewById<ConstraintLayout>(R.id.gridLayout)
        gridContainer.addView(masterGrid)
        val gridContainer_personal = fragmentView.findViewById<ConstraintLayout>(R.id.myGridLayout)
        gridContainer_personal.addView(localGrid)
    }

    override fun setCoordinateListeners() {
        for (y in 0 until Grid.DIMENSIONS) {
            for (x in 0 until Grid.DIMENSIONS) {
                gridCoords[y][x]?.setOnClickListener { view: View? ->
                    if (!canPlace) {
                        return@setOnClickListener
                    }
                    var status = 0
                    try {
                        status = matchViewModel?.turnHandler!!.otherPlayer!!.receiveFire(x, y)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    canPlace = if (status == -1) {
                        return@setOnClickListener
                    } else {
                        false
                    }
                    var gameFinished = false
                    try {
                        gameFinished = matchViewModel?.turnHandler!!.isFinished
                    } catch (e: Exception) {
                        Log.e("Swap", e.message!!)
                    }
                    val finalGameFinished = gameFinished
                    if (status == 0) {
                        matchViewModel?.turnHandler!!.currentPlayer!!.addMiss()
                        val imagestatus = gridCoords[y][x]?.findViewById<ImageView>(R.id.status)
                        imagestatus?.background = resources.getDrawable(
                            R.drawable.miss,
                            activity!!.theme
                        )
                        imagestatus?.alpha = 0f
                        imagestatus?.visibility = View.VISIBLE
                        imagestatus?.animate()?.alpha(1f)?.setDuration(500)
                            ?.setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    if (!finalGameFinished) {
                                        matchViewModel?.turnHandler!!.swapTurns()
                                        findNavController(view!!).navigate(R.id.action_gameFragment_to_awaitSwapFragment)
                                    } else {
                                        matchViewModel?.addWinner()
                                        findNavController(view!!).navigate(R.id.action_gameFragment_to_scoreFragment)
                                    }
                                }
                            })?.start()
                    } else {
                        matchViewModel?.turnHandler!!.currentPlayer!!.addHit()
                        wateranim!!.alpha = 1f
                        wateranim!!.visibility = View.VISIBLE
                        wateranim!!.animate().setDuration(5000).start()
                        val rotate = RotateAnimation(
                            0f, -90f, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                        )
                        rotate.duration = 1500
                        rotate.interpolator = LinearInterpolator()
                        shipanim!!.animation = rotate
                        shipanim!!.alpha = 1f
                        shipanim!!.visibility = View.VISIBLE
                        shipanim!!.animate()
                            .setDuration(1500)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    if (!finalGameFinished) {
                                        wateranim!!.alpha = 0f
                                        wateranim!!.visibility = View.INVISIBLE
                                        shipanim!!.alpha = 0f
                                        shipanim!!.visibility = View.INVISIBLE

                                        /*matchViewModel?.turnHandler!!.swapTurns()
                                        findNavController(view!!).navigate(R.id.action_gameFragment_to_awaitSwapFragment)*/
                                        canPlace = true;
                                    } else {
                                        matchViewModel?.addWinner()
                                        findNavController(view!!).navigate(R.id.action_gameFragment_to_scoreFragment)
                                    }
                                }
                            }).start()
                        val imagestatus = gridCoords[y][x]?.findViewById<ImageView>(R.id.status)
                        imagestatus?.background = resources.getDrawable(
                            R.drawable.hit,
                            activity!!.theme
                        )
                        imagestatus?.alpha = 0f
                        imagestatus?.visibility = View.VISIBLE
                        imagestatus?.animate()
                            ?.alpha(1f)
                            ?.setDuration(500)
                            ?.start()
                    }
                }
            }
        }
    }
}