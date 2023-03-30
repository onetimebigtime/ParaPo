package com.example.parapo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parapo.login.sign_up.SignUpFragment;
import com.google.android.gms.actions.NoteIntents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    //INITIALIZING COMPONENTS
    private static final String PAGE = "SignInActivity";
    private TextView signInShowHideLabel;
    private EditText signInEmailText, signInPasswordText;
    private ProgressBar signInProgressBar;
    private FirebaseAuth signInAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //TITLE BAR
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign In");
        //FIREBASE SETUP
        signInAuth = FirebaseAuth.getInstance();
        //SETTING UP TEXT BOXES
        signInEmailText = findViewById(R.id.signin_email_text); //EMAIL TEXT BOX
        signInPasswordText = findViewById(R.id.signin_password_text); //PASSWORD TEXT BOX
        signInShowHideLabel = findViewById(R.id.signin_showpass_label); //0--SHOW AND HIDE LINK
        //SETTING UP PROGRESS BAR
        signInProgressBar = findViewById(R.id.signin_progressbar);
        //1--SETTING UP SIGN IN BUTTON
        Button signInButton = findViewById(R.id.signin_button);
        //2--SETTING UP TO SIGN UP BUTTON
        Button toSignUpButton = findViewById(R.id.to_signup_button);

        //0---------------------------------SHOW AND HIDE PASSWORD FUNCTION SECTION---------------------------------------------
        signInShowHideLabel.setText(R.string.show_pass);
        signInShowHideLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInPasswordText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    signInPasswordText.setTransformationMethod((PasswordTransformationMethod.getInstance()));
                    signInShowHideLabel.setText(R.string.hide_pass);
                }
                else {
                    signInPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    signInShowHideLabel.setText(R.string.show_pass);
                }
            }
        });
        //0---------------------------------SHOW AND HIDE PASSWORD FUNCTION SECTION---------------------------------------------

        //1----------------------------------SIGN IN BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signInEmail = signInEmailText.getText().toString();
                String signInPassword = signInPasswordText.getText().toString();
                //CHECKING IF TEXT BOXES HAVE ERROR VALUES
                if (TextUtils.isEmpty(signInEmail) || !Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
                    Toast.makeText(SignInActivity.this, "Please provide your email", Toast.LENGTH_SHORT).show();
                    signInEmailText.setError("Enter a valid email address");
                    signInEmailText.requestFocus();
                }
                else if (TextUtils.isEmpty(signInPassword)) {
                    Toast.makeText(SignInActivity.this, "Please provide your correct password", Toast.LENGTH_SHORT).show();
                    signInPasswordText.setError("Enter a correct password");
                    signInPasswordText.requestFocus();
                }
                else {
                    signInProgressBar.setVisibility(View.VISIBLE);
                    //1--SIGNING IN USER FUNCTION
                    signInUser(signInEmail, signInPassword);
                }
            }
        });
        //1----------------------------------SIGN IN BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------

        //----------------------------------TO SIGN UP BUTTON ON CLICK FUNCTION SECTION-----------------------------------------
        toSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //CREATING AN INTENT TO OPEN MAIN ACTIVITY
                    Intent intent =new Intent(SignInActivity.this, SignUpFragment.class );

                    //Prevent user from returning back to register
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); //END SIGNUP ACTIVITY
                } catch (Exception e){
                    String title = "Something Went Wrong!";
                    String message = "Something went wrong while we are loading sign up page. Please check your internet connection and try again.";
                    popUpAlert(title, message);
                }
            }
        });
        //----------------------------------TO SIGN UP BUTTON ON CLICK FUNCTION SECTION-----------------------------------------
    }
    //1--------------------------------------SIGNING IN USER FUNCTION SECTION------------------------------------------------------
    private void signInUser(String email, String password) {
        signInAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        //GET THE CURRENT USER FROM THE FIREBASE
                        FirebaseUser firebaseTraveler = signInAuth.getCurrentUser();

                        //CHECK IF EMAIL IS VERIFIED
                        if (firebaseTraveler.isEmailVerified()) {
                            Toast.makeText(SignInActivity.this, "Welcome Traveller! Start your Travel with us.", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(SignInActivity.this, MainActivity.class );

                            //Prevent user from returning back to register
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); //END SIGNUP ACTIVITY

                        }
                        else {
                            firebaseTraveler.sendEmailVerification();
                            signInAuth.signOut();
                            String title = "Verify Email First";
                            String message = "Please verify your email before you can log in again. Thank you!";
                            popUpAlert(title, message);
                        }
                    } catch (Exception e) {
                        String title = "Something Went Wrong!";
                        String message = "Something went wrong while we are trying to log you in. Please check your internet connection and try again.";
                        popUpAlert(title, message);
                    }
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e) {
                        String title = "Unknown Traveler";
                        String message = "Can't find Traveler account! Make sure that you have a valid account to use ParaPo.";
                        popUpAlert(title, message);
                        signInEmailText.setError("Traveler doesn't exist!");
                        signInEmailText.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        signInEmailText.setError("Invalid log in credential");
                        signInPasswordText.setError("Invalid log in credential");
                        signInEmailText.requestFocus();
                        signInPasswordText.requestFocus();
                    }
                    catch(Exception e){
                        Log.e(PAGE, e.getMessage());
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                signInProgressBar.setVisibility(View.GONE);
            }
        });
    }
    //1--------------------------------------SIGNING IN USER FUNCTION SECTION------------------------------------------------------

    //Pop Alert
    private void popUpAlert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (Objects.equals(title, "Verify Email First")) {
            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_CONTACTS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    catch (Exception e) {
                        Toast.makeText(SignInActivity.this, "No Email application installed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        //OPEN EMAIL APPS
        AlertDialog popAlertDialog = alertDialog.create();
        popAlertDialog.show();
    }

}