package com.asteph11.finalproject.controllers.fragments

import androidx.navigation.Navigation.findNavController
import com.asteph11.finalproject.models.viewmodels.MatchViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.asteph11.finalproject.R
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class ScoreFragment : Fragment() {
    private var matchViewModel: MatchViewModel? = null
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
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val p1Name = view.findViewById<TextView>(R.id.player1title)
        val p2Name = view.findViewById<TextView>(R.id.player2title)
        val p1Hits = view.findViewById<TextView>(R.id.p1shipshittotal)
        val p2Hits = view.findViewById<TextView>(R.id.p2shipshittotal)
        val p1Miss = view.findViewById<TextView>(R.id.p1shipsmissedtotal)
        val p2Miss = view.findViewById<TextView>(R.id.p2shipsmissedtotal)
        val linearLayout = view.findViewById<LinearLayoutCompat>(R.id.winnersList)
        val rematchButton = view.findViewById<CardView>(R.id.button_newmatch)
        p1Name.text = "" + matchViewModel!!.turnHandler!!.p2!!.name
        p2Name.text = "" + matchViewModel!!.turnHandler!!.p1!!.name
        p1Hits.text = "" + matchViewModel!!.turnHandler!!.p2!!.hits
        p2Hits.text = "" + matchViewModel!!.turnHandler!!.p1!!.hits
        p1Miss.text = "" + matchViewModel!!.turnHandler!!.p2!!.misses
        p2Miss.text = "" + matchViewModel!!.turnHandler!!.p1!!.misses
        val names = matchViewModel!!.topList
        var i = 0
        while (i < names!!.size && i < 3) {
            val layout = layoutInflater.inflate(
                R.layout.layout_winnertextview,
                view as ViewGroup, false
            )
            val textView = layout.findViewById<TextView>(R.id.winnername)
            textView.text = names[i]
            linearLayout.addView(layout)
            i++
        }
        rematchButton.setOnClickListener { v: View? ->
            matchViewModel!!.reset()
            findNavController(v!!).navigate(R.id.action_scoreFragment_to_setupFragment)
        }
    }
}