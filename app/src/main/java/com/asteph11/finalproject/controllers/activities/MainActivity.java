package com.asteph11.finalproject.controllers.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.asteph11.finalproject.R;
import com.asteph11.finalproject.models.viewmodels.MatchViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private MatchViewModel matchViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewModels();

        setContentView(R.layout.activity_main);
    }

    private void initViewModels() {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication());

        matchViewModel = factory.create(MatchViewModel.class);
        matchViewModel.init(FirebaseFirestore.getInstance());
    }

}
