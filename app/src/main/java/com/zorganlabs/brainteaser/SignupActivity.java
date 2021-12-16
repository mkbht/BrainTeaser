package com.zorganlabs.brainteaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ScrollView registerLayout;
    private boolean error;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Register");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // firebase instance
        mAuth = FirebaseAuth.getInstance();

        // reference ui components
        EditText fullNameInput = (EditText) findViewById(R.id.fullNameInput);
        EditText emailInput = (EditText) findViewById(R.id.emailInput);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        EditText passwordConfirmInput = (EditText) findViewById(R.id.passwordConfirmInput);
        TextInputLayout fullNameInputLayout = (TextInputLayout) findViewById(R.id.fullNameInputLayout);
        TextInputLayout emailInputLayout = (TextInputLayout) findViewById(R.id.emailInputLayout);
        TextInputLayout passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        TextInputLayout passwordConfirmInputLayout = (TextInputLayout) findViewById(R.id.passwordConfirmInputLayout);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerLayout = (ScrollView) findViewById(R.id.registerLayout);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error = false;
                fullNameInputLayout.setError("");
                emailInputLayout.setError("");
                passwordInputLayout.setError("");
                passwordConfirmInputLayout.setError("");

                if (fullNameInput.getText().toString().trim().equals("")) {
                    emailInputLayout.setError("Name is required.");
                    error = true;
                }
                if (emailInput.getText().toString().trim().equals("")) {
                    emailInputLayout.setError("Email is required.");
                    error = true;
                }
                if (passwordInput.getText().toString().trim().equals("")) {
                    passwordInputLayout.setError("Password is required.");
                    error = true;
                } else if (!passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
                    passwordConfirmInputLayout.setError("Confirm password do not match.");
                    error = true;
                }
                if (!error) {
                    register(fullNameInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString());
                }
            }
        });
    }

    private void register(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateProfile(fullName);
                            Toast.makeText(SignupActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(registerLayout, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.error))
                                    .show();
                        }
                    }
                });
    }

    private void updateProfile(String fullName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }
}