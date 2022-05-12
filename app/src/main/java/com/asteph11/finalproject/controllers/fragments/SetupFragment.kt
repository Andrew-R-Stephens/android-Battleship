package com.asteph11.finalproject.controllers.fragments

import androidx.navigation.Navigation.findNavController
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import com.asteph11.finalproject.R
import androidx.cardview.widget.CardView
import android.widget.EditText
import android.widget.ImageView
import com.asteph11.finalproject.models.data.Grid
import java.lang.Exception

class SetupFragment : AGridFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readyButton = view.findViewById<CardView>(R.id.readyButton)
        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val inflater = LayoutInflater.from(context)
        masterGrid = inflater.inflate(
            R.layout.layout_grid,
            view as ViewGroup,
            false
        )
        createGrid(view, masterGrid!!)
        setCoordinateListeners()
        readyButton.setOnClickListener { v: View? ->
            if (nameInput.text.toString().equals("", ignoreCase = true) ||
                !matchViewModel?.turnHandler!!.currentPlayer!!.isGridReady
            ) {
                return@setOnClickListener
            }
            var gameReady = false
            try {
                gameReady = matchViewModel?.playersReady()!!
            } catch (e: Exception) {
                Log.e("Swap", e.message!!)
            }
            matchViewModel?.turnHandler!!.swapTurns()
            if (!gameReady) {
                matchViewModel?.turnHandler!!.otherPlayer!!.name = nameInput.text.toString()
                nameInput.setText("")
                findNavController(v!!).navigate(R.id.action_setupFragment_to_awaitSwapFragment)
            } else {
                matchViewModel?.turnHandler!!.otherPlayer!!.name = nameInput.text.toString()
                findNavController(v!!).navigate(R.id.action_setupFragment_to_gameFragment)
            }
        }
    }

    override fun setCoordinateListeners() {
        for (y in 0 until Grid.DIMENSIONS) {
            for (x in 0 until Grid.DIMENSIONS) {
                gridCoords[y][x]?.setOnClickListener { view: View? ->
                    val status = gridCoords[y][x]?.findViewById<ImageView>(R.id.status)
                    matchViewModel?.turnHandler!!.currentPlayer!!.setShip(x, y)
                    if (matchViewModel?.turnHandler!!.currentPlayer!!.grid!!.getStatusAt(
                            x,
                            y
                        ) === Grid.Status.SHIP
                    ) {
                        status?.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ship,
                                requireActivity().theme
                            )
                        )
                        status?.visibility = View.VISIBLE
                    } else {
                        status?.setImageDrawable(null)
                        status?.visibility = View.INVISIBLE
                    }
                    try {
                        println(matchViewModel?.turnHandler!!.currentPlayer!!.grid)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}