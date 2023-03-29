package com.example.parapo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private EditText signInEmailText, signInPasswordText;
    private ProgressBar signInProgressBar;
    private FirebaseAuth signInAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign In");

        //Setup Firebase
        signInAuth = FirebaseAuth.getInstance();
        // Setup variables
        signInEmailText = findViewById(R.id.signin_email_text); //find email textbox
        signInPasswordText = findViewById(R.id.signin_password_text); //find password textbox
        signInProgressBar = findViewById(R.id.signin_progressbar); //find progress bar

        //Setup sign in button
        Button signInButton = findViewById(R.id.signin_button);
        //Sign in button function
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signInEmail = signInEmailText.getText().toString();
                String signInPassword = signInPasswordText.getText().toString();
                // Checking Empty textboxes
                if (TextUtils.isEmpty(signInEmail) || !Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
                    Toast.makeText(SignInActivity.this, "Please provide your email", Toast.LENGTH_SHORT).show();
                    signInEmailText.setError("Enter a valid email address");
                    signInEmailText.requestFocus();
                } else if (TextUtils.isEmpty(signInPassword)) {
                    Toast.makeText(SignInActivity.this, "Please provide your correct password", Toast.LENGTH_SHORT).show();
                    signInPasswordText.setError("Enter a correct password");
                    signInPasswordText.requestFocus();
                } /*else if (!Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
                    Toast.makeText(SignInActivity.this, "Please provide your email", Toast.LENGTH_SHORT).show();
                    signInEmailText.setError("Enter a valid email");
                }*/
                else {
                    signInProgressBar.setVisibility(View.VISIBLE);
                    signInUser(signInEmail, signInPassword);
                }
            }
        });

        //Setup to sign up button
        Button toSignUpButton = findViewById(R.id.to_signup_button);
        //to sign up Button functionality
        toSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void signInUser(String email, String password) {
        signInAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Welcome Traveller! Start your Travel with us.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                signInProgressBar.setVisibility(View.GONE);
            }
        });
    }
}