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
    EditText fullNameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText passwordConfirmInput;
    TextInputLayout fullNameInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout passwordConfirmInputLayout;
    Button registerButton;

    @Override
    public void onStart() {
        super.onStart();
        // check if the user is signed in
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
        fetchElementsById();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error = false;
                checkValidations();
            }
        });
    }

    public void checkValidations() {
        // remove all the errors
        fullNameInputLayout.setError("");
        emailInputLayout.setError("");
        passwordInputLayout.setError("");
        passwordConfirmInputLayout.setError("");

        // full name input
        if (fullNameInput.getText().toString().trim().equals("")) {
            emailInputLayout.setError("Name is required.");
            error = true;
        }

        // email input
        if (emailInput.getText().toString().trim().equals("")) {
            emailInputLayout.setError("Email is required.");
            error = true;
        }

        // password input
        if (passwordInput.getText().toString().trim().equals("")) {
            passwordInputLayout.setError("Password is required.");
            error = true;
        } else if (!passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
            passwordConfirmInputLayout.setError("Confirm password do not match.");
            error = true;
        }

        // if no error is found then register
        if (!error) {
            register(fullNameInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    public void fetchElementsById() {
        // fetch elements by id
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordConfirmInputLayout = findViewById(R.id.passwordConfirmInputLayout);
        registerButton = findViewById(R.id.registerButton);
        registerLayout = findViewById(R.id.registerLayout);
    }

    private void register(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // update the profile
                            updateProfile(fullName);
                            // display toast
                            Toast.makeText(SignupActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // display errors
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

        // update current profile
        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}