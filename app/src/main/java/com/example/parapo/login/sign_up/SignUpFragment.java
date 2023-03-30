package com.example.parapo.login.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parapo.MainActivity;
import com.example.parapo.R;
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

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpFragment extends AppCompatActivity{

    //Initializing Components
    private TextView signUpCancelLink;

    private RadioGroup signUpGenderRadio;
    private RadioButton signUpGenderReveal;
    private EditText signUpFullNameText, signUpEmailText, signUpBirthdateText, signUpPasswordText, signUpConfirmPassText;
    private ProgressBar signUpProgressBar;
    private static final String TAG = "SignUpFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);
        
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up");

        Toast.makeText(SignUpFragment.this, "Sign Up now to become a Traveler", Toast.LENGTH_SHORT).show();

        // Return to Sign In page
        /*signUpCancelLink = findViewById(R.id.signup_cancel_link);
        signUpCancelLink.setOnClickListener(this); // Set the value of v in OnClick Function*/

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

        //SIGN UP BUTTON FUNCTION
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GENDER REVEAL
                int genderReveal = signUpGenderRadio.getCheckedRadioButtonId();
                signUpGenderReveal = findViewById(genderReveal);

                String gender; //OBTAIN VALUE LATER
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
                    //Erasing password input
                    signUpConfirmPassText.clearComposingText();
                } else {
                    gender = signUpGenderReveal.getText().toString();
                    signUpProgressBar.setVisibility(View.VISIBLE);
                    signUpTraveler(full_name, email, birthdate, gender, password);

                }
            }
        });
    }
    //Register traveler method
    private void signUpTraveler(String full_name, String email, String birthdate, String gender, String password) {
        //Firebase initiation
        FirebaseAuth signUpAuth = FirebaseAuth.getInstance(); // Firebase object
        signUpAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpFragment.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpFragment.this, "Congrats! You are now a Traveler!", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseTraveler = signUpAuth.getCurrentUser(); //firebase user object

                    //Save user data in realtime database
                    TravelersData getTravelersData = new TravelersData(full_name, gender, birthdate);

                    //Get user reference in the database
                    DatabaseReference travelerReference = FirebaseDatabase.getInstance().getReference("Travelers");
                    travelerReference.child(firebaseTraveler.getUid()).setValue(getTravelersData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // Send Verification
                                firebaseTraveler.sendEmailVerification();
                                Toast.makeText(SignUpFragment.this, "Traveler, please verify your email", Toast.LENGTH_SHORT).show();

                                //Open Main activity after Profile  successful registration
                                Intent intent =new Intent(SignUpFragment.this, MainActivity.class );

                                //Prevent user from returning back to register
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); //close this signup activity
                            } else {
                                //Make progressbar disappear
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(SignUpFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Set progressbar visibility
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthUserCollisionException e) {
                        signUpEmailText.setError("A Traveler exists with this email!");
                        signUpEmailText.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUpFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Make progressbar disappear
                    signUpProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}