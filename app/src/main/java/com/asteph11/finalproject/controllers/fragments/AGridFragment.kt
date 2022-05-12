package com.asteph11.finalproject.controllers.fragments

import com.asteph11.finalproject.models.viewmodels.MatchViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.asteph11.finalproject.R
import androidx.appcompat.widget.LinearLayoutCompat
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.asteph11.finalproject.models.data.Grid

abstract class AGridFragment : Fragment() {
    protected var matchViewModel: MatchViewModel? = null
    protected var coordSize = 0
    protected var masterGrid: View? = null
    protected lateinit var gridRows: Array<View?>
    protected lateinit var gridCoords: Array<Array<View?>>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // OBTAIN VIEW MODEL REFERENCE
        if (matchViewModel == null) {
            matchViewModel = ViewModelProvider(requireActivity()).get(MatchViewModel::class.java)
            // INITIALIZE VIEW MODEL
            matchViewModel!!.init(FirebaseFirestore.getInstance())
        }
        val display = requireActivity().windowManager.defaultDisplay
        coordSize = (display.width / (Grid.DIMENSIONS.toDouble() + 1)).toInt()
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    protected fun createGrid(fragmentView: View, containerGrid: View) {
        gridCoords = Array(Grid.DIMENSIONS) { arrayOfNulls(Grid.DIMENSIONS) }
        val markers = arrayOf("A", "B", "C", "D", "E", "F", "G")
        val grid_linearLayout = containerGrid.findViewById<LinearLayoutCompat>(R.id.gridContainer)
        val inflater = LayoutInflater.from(context)
        // Set top markers
        val topRowInflater = inflater.inflate(
            R.layout.layout_gridrow,
            fragmentView as ViewGroup,
            false
        )
        val topRow_linearLayout =
            topRowInflater.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)
        for (x in 0 until Grid.DIMENSIONS + 1) {
            val marker_top = inflater.inflate(
                R.layout.layout_gridcoord,
                fragmentView,
                false
            )
            marker_top.layoutParams = LinearLayout.LayoutParams(
                coordSize,
                coordSize, 1f
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
        }
        Log.d("Parent", topRow_linearLayout.parent.toString())
        grid_linearLayout.addView(topRowInflater)
        gridRows = arrayOfNulls(Grid.DIMENSIONS)
        for (y in 0 until Grid.DIMENSIONS) {
            gridRows[y] = inflater.inflate(
                R.layout.layout_gridrow,
                fragmentView,
                false
            )
            val row_linearLayout =
                gridRows[y]?.findViewById<LinearLayoutCompat>(R.id.gridrowContainer)

            // Set left markers
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

            // Add columns
            for (x in 0 until Grid.DIMENSIONS) {
                gridCoords[y][x] = inflater.inflate(
                    R.layout.layout_gridcoord,
                    fragmentView,
                    false
                )
                gridCoords[y][x]?.setLayoutParams(
                    LinearLayout.LayoutParams(
                        coordSize,
                        coordSize, 1f
                    )
                )
                gridCoords[y][x]?.findViewById<View>(R.id.status)?.visibility = View.INVISIBLE
                row_linearLayout?.addView(gridCoords[y][x])
            }
            grid_linearLayout.addView(gridRows[y])
        }
        val gridContainer = fragmentView.findViewById<ConstraintLayout>(R.id.gridLayout)
        gridContainer.addView(containerGrid)
    }

    protected abstract fun setCoordinateListeners()
}