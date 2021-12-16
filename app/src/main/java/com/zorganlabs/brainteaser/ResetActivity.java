package com.zorganlabs.brainteaser;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ScrollView resetLayout;
    private boolean error;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }
}
