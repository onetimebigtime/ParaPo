package com.example.parapo.login.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parapo.MainActivity;
import com.example.parapo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class SignUpFragment extends AppCompatActivity implements View.OnClickListener {

    //Initializing Components
    private TextView signUpCancelLink, signUpButton;
    private EditText signUpFullNameText, signUpEmailText, signUpBirthdateText, signUpPasswordText, signUpConfirmPassText;
    private ProgressBar signUpProgressBar;
    private FirebaseAuth paraPoAuth; //Firebase initiation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);

        paraPoAuth = FirebaseAuth.getInstance(); // Firebase object

        Toast.makeText(SignUpFragment.this, "Sign Up now", Toast.LENGTH_SHORT).show();

        // Return to Sign In page
        signUpCancelLink = findViewById(R.id.signup_cancel_link);
        signUpCancelLink.setOnClickListener(this); // Set the value of v in OnClick Function

        //Setting up Button
        signUpButton = findViewById(R.id.signup_button); //Sign Up Button
        signUpButton.setOnClickListener(this); // Set the value of v in OnClick Function

        //Setting up textboxes
        signUpFullNameText = findViewById(R.id.signup_fullname_text); //fullname textbox
        signUpEmailText = findViewById(R.id.signup_email_text);// email textbox
        signUpBirthdateText = findViewById(R.id.signup_birthdate_text); // birth textbox
        signUpPasswordText = findViewById(R.id.signup_password_text); //password textbox
        signUpConfirmPassText = findViewById(R.id.confirmpass_text);

        // Setting up for progressbar
        signUpProgressBar = (ProgressBar) findViewById(R.id.signup_progressbar);
    }

    //OnClick Function
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_cancel_link:
                startActivity(new Intent(this, MainActivity.class)); //Change to Sign In
                break;
            case R.id.signup_button:
                signUpTraveler();
                break;
        }
    }

    private void signUpTraveler() {
        String fullName = signUpFullNameText.getText().toString().trim();
        String email = signUpEmailText.getText().toString().trim();
        String birthdate = signUpBirthdateText.getText().toString().trim();
        String password = signUpPasswordText.getText().toString().trim();
        String confirmPass = signUpConfirmPassText.getText().toString().trim();

        //Check if textbox is empty
        if(fullName.isEmpty()) {
            signUpFullNameText.setError("Please provide a password!");
            signUpFullNameText.requestFocus();
        }
        else if (email.isEmpty()) {
            signUpEmailText.setError("Please provide an email!");
            signUpEmailText.requestFocus();
        }
        else if(birthdate.isEmpty()) {
            signUpBirthdateText.setError("Please provide a birthdate!");
            signUpBirthdateText.requestFocus();
        }
        else if(password.isEmpty()) {
            signUpPasswordText.setError("Please provide a password!");
            signUpPasswordText.requestFocus();
        }
        else if(confirmPass.isEmpty()) {
            signUpConfirmPassText.setError("Please repeat the password!");
            signUpConfirmPassText.requestFocus();
        }

        //Check Email if valid
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmailText.setError("Please provide a valid Email!");
            signUpEmailText.requestFocus();
        }
        //Check confirmed password
        else if(!password.equals(confirmPass)) {
            signUpConfirmPassText.setError("Please repeat the password!");
            signUpConfirmPassText.requestFocus();
            //Erasing password input
            signUpConfirmPassText.clearComposingText();
        }
        //Check length of password
        else if(password.length() < 6) {
            signUpPasswordText.setError("Password should be atleast 6 letters");
            signUpPasswordText.requestFocus();
        }
        //ProgressBar Setup
        signUpProgressBar.setVisibility(View.VISIBLE);
        paraPoAuth.createUserWithEmailAndPassword(email, confirmPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            TravellerData travellerData = new TravellerData(fullName, email, birthdate,password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(travellerData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(SignUpFragment.this, "You are now a Traveller", Toast.LENGTH_SHORT).show();
                                                signUpProgressBar.setVisibility(View.GONE);
                                            }

                                            else {
                                                Toast.makeText(SignUpFragment.this, "Failed to Sign Up, Try again later!", Toast.LENGTH_SHORT).show();
                                                signUpProgressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else{
                            Toast.makeText(SignUpFragment.this, "Failed to Sign Up, Try again later!", Toast.LENGTH_SHORT).show();
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}