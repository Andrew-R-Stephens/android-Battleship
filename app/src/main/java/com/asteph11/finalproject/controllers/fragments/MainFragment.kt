package com.asteph11.finalproject.controllers.fragments

import com.asteph11.finalproject.models.viewmodels.MatchViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.asteph11.finalproject.R
import androidx.cardview.widget.CardView
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.asteph11.finalproject.controllers.activities.GameActivity

class MainFragment : Fragment() {
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
        // INITIALIZE VIEW MODEL
        if (context != null) {
            matchViewModel!!.init(FirebaseFirestore.getInstance())
        }
        return inflater.inflate(R.layout.fragment_startscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startButton = view.findViewById<CardView>(R.id.button_start)
        startButton.setOnClickListener { v: View? ->
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }
    }
}