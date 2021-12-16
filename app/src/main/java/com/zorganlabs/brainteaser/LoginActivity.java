package com.zorganlabs.brainteaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Boolean error;
    ScrollView loginLayout;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // hide action bar
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        EditText emailInput = (EditText) findViewById(R.id.emailInput);
        TextInputLayout emailInputLayout = (TextInputLayout) findViewById(R.id.emailInputLayout);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        TextInputLayout passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        loginLayout = (ScrollView) findViewById(R.id.loginLayout);

        // login button click handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error = false;
                emailInputLayout.setError("");
                passwordInputLayout.setError("");

                if (emailInput.getText().toString().trim().equals("")) {
                    emailInputLayout.setError("Email is required.");
                    error = true;
                }
                if (passwordInput.getText().toString().trim().equals("")) {
                    passwordInputLayout.setError("Password is required.");
                    error = true;
                }
                if (!error) {
                    signIn(emailInput.getText().toString(), passwordInput.getText().toString());
                }
            }
        });

        // register button click handler
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Sign in
     * @param email Email address
     * @param password password
     */
    private void signIn(String email, String password)  {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(loginLayout, task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.error))
                                    .show();
                        }
                    }
                });
    }


}