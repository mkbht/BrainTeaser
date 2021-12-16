package com.zorganlabs.brainteaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Boolean error;
    ScrollView loginLayout;
    public ProgressDialog loginprogress;

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
        TextView resetPassword = (TextView) findViewById(R.id.resetPassword);
        TextInputLayout passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        loginLayout = (ScrollView) findViewById(R.id.loginLayout);
        loginprogress=new ProgressDialog(this);

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

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordDialog();
            }
        });

    }
    ProgressDialog loadingBar;
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

    private void resetPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enter email to reset password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // write the email using which you registered
//        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
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
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void loginUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginprogress.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    loginprogress.hide();
                    Toast.makeText(LoginActivity.this,"Cannot Sign In..Plaese Try Again",Toast.LENGTH_LONG);
                }
            }
        });
    }
}