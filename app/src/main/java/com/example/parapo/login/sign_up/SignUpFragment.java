package com.example.parapo.login.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parapo.MainActivity;
import com.example.parapo.R;
import com.example.parapo.SignInActivity;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpFragment extends AppCompatActivity{
    //Initializing Components
    private DatePickerDialog birthdatePicker;
    private TextView signUpCancelLink;
    private RadioGroup signUpGenderRadio;
    private RadioButton signUpGenderReveal;
    private EditText signUpFullNameText, signUpEmailText, signUpBirthdateText, signUpPasswordText, signUpConfirmPassText;
    private ProgressBar signUpProgressBar;
    private boolean isOnline;
    private double latitude, longitude;
    private static final String TAG = "SignUpFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);

        //SETTING UP TITLE BAR
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up");
        //MESSAGE
        Toast.makeText(SignUpFragment.this, "Sign Up now to become a Traveler", Toast.LENGTH_SHORT).show();

        // SETTING UP CANCEL LINK
        signUpCancelLink = findViewById(R.id.signup_cancel_link);

        //SETTING UP THE TEXT BOXES
        signUpFullNameText = findViewById(R.id.signup_fullname_text); //FULLNAME TEXTBOX
        signUpEmailText = findViewById(R.id.signup_email_text); //EMAIL TEXTBOX
        signUpBirthdateText = findViewById(R.id.signup_birthdate_text); //BIRTH TEXTBOX
        signUpPasswordText = findViewById(R.id.signup_password_text); //PASSWORD TEXTBOX
        signUpConfirmPassText = findViewById(R.id.confirmpass_text);

        //SETTING UP RADIO GENDER GROUP BUTTON
        signUpGenderRadio = findViewById(R.id.signup_gender_radio);
        signUpGenderRadio.clearCheck();

        //SETTING UP PROGRESS BAR
        signUpProgressBar = findViewById(R.id.signup_progressbar);

        //SETTING UP SIGN UP BUTTON
        Button signUpButton = findViewById(R.id.signup_button);

        //-------------------------------SETTING UP A DATE PICKER ON CLICK FUNCTION SECTION---------------------------------
        signUpBirthdateText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            birthdatePicker = new DatePickerDialog(SignUpFragment.this, (view, year1900, month1, dayOfMonth) -> signUpBirthdateText.setText((month1 +1) + "/" + dayOfMonth + "/" + year1900), month,day,year);
            birthdatePicker.show();
        });
        //-------------------------------SETTING UP A DATE PICKER ON CLICK FUNCTION SECTION---------------------------------

        //1----------------------------------SIGN UP BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------
        signUpButton.setOnClickListener(v -> {

            //USER INDICATOR IF ONLINE
            isOnline = false;

            //USER DEFAULT LOCATION
            latitude = 0d;
            longitude = 0d;

            //GENDER REVEAL
            int genderReveal = signUpGenderRadio.getCheckedRadioButtonId();
            signUpGenderReveal = findViewById(genderReveal);

            //BASIC USER INFORMATION
            String full_name = signUpFullNameText.getText().toString().trim();
            String email = signUpEmailText.getText().toString().trim();
            String birthdate = signUpBirthdateText.getText().toString().trim();
            String password = signUpPasswordText.getText().toString().trim();
            String confirmPass = signUpConfirmPassText.getText().toString().trim();

            //CHECK TEXT BOX IF EMPTY
            if(TextUtils.isEmpty(full_name)) {
                Toast.makeText(SignUpFragment.this, "Please provide a full name", Toast.LENGTH_SHORT).show();
                signUpFullNameText.setError("Enter a full name!");
                signUpFullNameText.requestFocus();
            }
            else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignUpFragment.this, "Please provide an email address", Toast.LENGTH_SHORT).show();
                signUpEmailText.setError("Enter a valid email address!");
                signUpEmailText.requestFocus();
            }
            else if(TextUtils.isEmpty(birthdate)) {
                Toast.makeText(SignUpFragment.this, "Please provide a birthdate", Toast.LENGTH_SHORT).show();
                signUpBirthdateText.setError("Enter a birthdate!");
                signUpBirthdateText.requestFocus();
            }
            else if (signUpGenderRadio.getCheckedRadioButtonId()==-1) {
                signUpGenderReveal.setError("Enter a gender!");
                signUpGenderReveal.requestFocus();
            }
            else if(TextUtils.isEmpty(password) || password.length() < 6) {
                Toast.makeText(SignUpFragment.this, "Please provide a password", Toast.LENGTH_SHORT).show();
                signUpPasswordText.setError("Enter a more than 6 character password!");
                signUpPasswordText.requestFocus();
            }
            else if(TextUtils.isEmpty(confirmPass)) {
                Toast.makeText(SignUpFragment.this, "Please provide a password", Toast.LENGTH_SHORT).show();
                signUpConfirmPassText.setError("Enter  a password to confirm!");
                signUpConfirmPassText.requestFocus();
            }
            //CHECK IF PASSWORD IS THE SAME AS CONFIRM PASSWORD
            else if(!password.equals(confirmPass)) {
                Toast.makeText(SignUpFragment.this, "Please confirm pasword", Toast.LENGTH_SHORT).show();
                signUpConfirmPassText.setError("Enter password for verification!");
                signUpConfirmPassText.requestFocus();
                //ERASING TEXT IN THE CONFIRM TEXT BOX
                signUpConfirmPassText.clearComposingText();
            }
            //NO ERROR PROCEED
            else {
                String gender = signUpGenderReveal.getText().toString();
                signUpProgressBar.setVisibility(View.VISIBLE);
                //1--SIGNING UP TRAVELERS
                signUpTraveler(full_name, email, birthdate, gender, password, latitude, longitude, isOnline);

            }
        });
        //1----------------------------------SIGN UP BUTTON ON CLICK FUNCTION SECTION---------------------------------------------------
        //1----------------------------------CANCEL LINK ON CLICK FUNCTION SECTION---------------------------------------------------
        signUpCancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SignUpFragment.this, SignInActivity.class );

                //Prevent user from returning back to register
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); //END SIGNUP ACTIVITY
            }
        });
        //1----------------------------------CANCEL LINK ON CLICK FUNCTION SECTION---------------------------------------------------
    }
    //1------------------------------------SIGNING UP TRAVELERS METHOD SECTION-----------------------------------------------------------
    private void signUpTraveler(String full_name, String email, String birthdate, String gender, String password, double latitude, double longitude, boolean isOnline) {
        //Firebase initiation
        FirebaseAuth signUpAuth = FirebaseAuth.getInstance(); // Firebase object

        //---------------------------FUNCTION TO CREATE A USER IN THE DATA BASE------------------------------------------------------------
        signUpAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpFragment.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //IF THE CREATION OF EMAIL AND PASSWORD TO THE DATABASE IS SUCCESSFUL
                if(task.isSuccessful()){
                    Toast.makeText(SignUpFragment.this, "Congrats! You are now a Traveler!", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = signUpAuth.getCurrentUser(); //GET CURRENT USER IN FIREBASE
                    String userId = firebaseUser.getUid(); //GET USER ID IN FIREBASE

                    //GETTING USER INFORMATION FROM INPUTS AND SETTING IT UP IN THE setTravelersData CONTAINER
                    TravelersData setTravelersData = new TravelersData(userId,full_name, birthdate, gender, latitude, longitude, isOnline);

                    //GET OR SET USER REFERENCE IN THE DATABASE
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Travelers");
                    //WRITING THE DATA IN THE FIREBASE DATABASE
                    databaseReference.child(userId).setValue(setTravelersData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //IF THE USERS DETAILS IS SUCCESSFUL SAVE IN THE FIREBASE DATABASE
                            if (task.isSuccessful()){
                                // SEND A VERIFICATION IN YOUR EMAIL
                                //firebaseUser.sendEmailVerification();
                                //Toast.makeText(SignUpFragment.this, "Traveler, please verify your email", Toast.LENGTH_SHORT).show();

                                //CREATING AN INTENT TO OPEN MAIN ACTIVITY
                                Intent intent =new Intent(SignUpFragment.this, MainActivity.class );

                                //Prevent user from returning back to register
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); //END SIGNUP ACTIVITY
                            }
                            //IF UNSUCCESSFUL OR HAVE ERRORS
                            else {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(SignUpFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Set progressbar visibility TO DISAPPEAR
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    });

                }
                //IF CREATION IS UNSUCCESSFUL OR HAVE ERRORS
                else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthUserCollisionException e) {
                        signUpEmailText.setError("A Traveler exist with this email!");
                        signUpEmailText.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUpFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ///Set progressbar visibility TO DISAPPEAR
                    signUpProgressBar.setVisibility(View.GONE);
                }
            }
        });
        //---------------------------FUNCTION TO CREATE A USER IN THE DATA BASE------------------------------------------------------------
    }
}