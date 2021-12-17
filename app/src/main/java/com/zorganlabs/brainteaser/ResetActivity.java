package com.zorganlabs.brainteaser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // sign user out if no current user found
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call super class
        super.onCreate(savedInstanceState);
    }
}
