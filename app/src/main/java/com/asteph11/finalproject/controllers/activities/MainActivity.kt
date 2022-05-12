package com.asteph11.finalproject.controllers.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.asteph11.finalproject.R
import com.asteph11.finalproject.models.viewmodels.MatchViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private var matchViewModel: MatchViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
        setContentView(R.layout.activity_main)
    }

    private fun initViewModels() {
        val factory = AndroidViewModelFactory.getInstance(this.application)
        matchViewModel = factory.create(MatchViewModel::class.java)
        matchViewModel!!.init(FirebaseFirestore.getInstance())
    }
}