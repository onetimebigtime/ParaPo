package com.example.parapo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
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
    private PopUpAlert popUpAlert;

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
        signInShowHideLabel.setOnClickListener(v -> {
            if (signInPasswordText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                signInPasswordText.setTransformationMethod((PasswordTransformationMethod.getInstance()));
                signInShowHideLabel.setText(R.string.show_pass);
            }
            else {
                signInPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                signInShowHideLabel.setText(R.string.hide_pass);
            }
        });
        //0---------------------------------SHOW AND HIDE PASSWORD FUNCTION SECTION---------------------------------------------

        //1----------------------------------SIGN IN BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------
        signInButton.setOnClickListener(v -> {
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
        });
        //1----------------------------------SIGN IN BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------

        //----------------------------------TO SIGN UP BUTTON ON CLICK FUNCTION SECTION-----------------------------------------
        toSignUpButton.setOnClickListener(v -> {
            try {
                //CREATING AN INTENT TO OPEN SIGN UP ACTIVITY
                Intent intent =new Intent(SignInActivity.this, SignUpFragment.class );

                //PREVENT USER FROM GOING BACK TO THE SIGN IN ACTIVITY
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); //END SIGN IN ACTIVITY
            } catch (Exception e){
                String title = "Something Went Wrong!";
                String message = "Something went wrong while we are loading sign up page. Please check your internet connection and try again.";
                popUpAlert = new PopUpAlert(title, message, this);
            }
        });
        //----------------------------------TO SIGN UP BUTTON ON CLICK FUNCTION SECTION-----------------------------------------
    }
    //1--------------------------------------SIGNING IN USER FUNCTION SECTION------------------------------------------------------
    private void signInUser(String email, String password) {
        signInAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, task -> {
            if (task.isSuccessful()) {
                try {
                    Toast.makeText(SignInActivity.this, "Welcome Traveller! Start your Travel with us.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));

                } catch (Exception e) {
                    String title = "Something Went Wrong!";
                    String message = "Something went wrong while we are trying to log you in. Please check your internet connection and try again.";
                    popUpAlert = new PopUpAlert(title, message, this);
                }
            }
            else {
                try {
                    throw Objects.requireNonNull(task.getException());
                }
                catch (FirebaseAuthInvalidUserException e) {
                    String title = "Unknown Traveler";
                    String message = "Can't find Traveler account! Make sure that you have a valid account to use ParaPo.";
                    popUpAlert = new PopUpAlert(title, message, this);
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
        });
    }
    //1--------------------------------------SIGNING IN USER FUNCTION SECTION------------------------------------------------------

    //--------------------------------------ON START FUNCTION SECTION---------------------------------------------------
    //REPLACING THE ACTIVITY THAT IS ON AN ON_START STATE AFTER A SUCCESSFUL SIGN IN
    @Override
    protected void onStart() {
        super.onStart();
        //REDIRECTING TO THE MAIN ACTIVITY UI AND REPLACING AS THE NEW ON_START STATE AFTER A SUCCESSFUL SIGN IN
        if (signInAuth.getCurrentUser() != null) {
            Toast.makeText(SignInActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
        //MAINTAIN THE AS THE ON_START STATE OF THE SYSTEM
        else {
            Toast.makeText(SignInActivity.this, "Login Now!", Toast.LENGTH_SHORT).show();
        }
    }
    //--------------------------------------ON START FUNCTION SECTION---------------------------------------------------
}