package com.zorganlabs.brainteaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Boolean error;
    ScrollView loginLayout;
    public ProgressDialog loginProgress;
    EditText emailInput;
    TextInputLayout emailInputLayout;
    EditText passwordInput;
    TextView resetPassword;
    TextInputLayout passwordInputLayout;
    Button loginButton;
    Button registerButton;
    ProgressDialog loadingBar;

    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // if user exists move to login page
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

        // get instance of firebase auth
        mAuth = FirebaseAuth.getInstance();

        // fetch elements by id
        this.fetchElementsById();
        loginProgress =new ProgressDialog(this);

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

        // reset button click handler
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordDialog();
            }
        });
    }

    private void fetchElementsById() {
        // fetch elements by id
        emailInput = findViewById(R.id.emailInput);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInput = findViewById(R.id.passwordInput);
        resetPassword = findViewById(R.id.resetPassword);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        loginLayout = findViewById(R.id.loginLayout);
    }

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

    private void resetPasswordDialog() {
        // set alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter email to reset password");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // set margin
        lp.setMargins(40, 40, 40, 40);

        // add linear layout dynamically
        LinearLayout linearLayout = new LinearLayout(this);
        // add edit text to the layout
        final EditText emailEt = new EditText(this);
        emailEt.setLayoutParams(lp);

        // input the email by which the user registered
        emailEt.setMinEms(16);
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        // add edit text to the layout
        linearLayout.addView(emailEt);
        // set padding
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        // listener when user chooses to reset the password
        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // fetch email from edit text view
                String email = emailEt.getText().toString().trim();
                if(email.equals("")) {
                    Snackbar.make(loginLayout, "Email address is required", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.error))
                            .show();
                } else {
                    beginRecovery(email);
                }
            }
        });

        // when user cancels to reset
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // show builder
        builder.create().show();
    }

    private void beginRecovery(String email) {
        // show progress bar
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this,"A recovery email is being sent to you email address.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occured.",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // display in case of failure
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
}