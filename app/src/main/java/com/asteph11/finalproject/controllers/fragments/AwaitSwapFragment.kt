package com.asteph11.finalproject.controllers.fragments

import androidx.navigation.Navigation.findNavController
import com.asteph11.finalproject.models.viewmodels.MatchViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.asteph11.finalproject.R
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.lang.Exception

class AwaitSwapFragment : Fragment() {
    private var matchViewModel: MatchViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // OBTAIN VIEW MODEL REFERENCE
        if (matchViewModel == null) {
            matchViewModel = ViewModelProvider(requireActivity()).get(MatchViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_awaitswap, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val request = view.findViewById<TextView>(R.id.text_request)
        if (!matchViewModel!!.turnHandler!!.currentPlayer!!.isReady) {
            request.text = "Please hand device over to other player"
        } else {
            request.text =
                "Please hand device over to " + matchViewModel!!.turnHandler!!.currentPlayer!!.name
        }
        val swappedButton = view.findViewById<CardView>(R.id.button_swapped)
        swappedButton.setOnClickListener { v: View? ->
            var isGameReady = false
            try {
                isGameReady = matchViewModel!!.playersReady()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (!isGameReady) {
                findNavController(v!!).popBackStack()
            } else {
                findNavController(v!!).navigate(R.id.action_awaitSwapFragment_to_gameFragment)
            }
        }
    }
}