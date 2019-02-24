package com.example.bishoysefin.pharmd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by bishoysefin on 2018-09-12.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progBar;
    EditText signUpEmail, signUpPassword, signUpPasswordConfirm, firstName, lastName, phoneNumber;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        signUpEmail = (EditText) findViewById(R.id.textbox3);
        signUpPassword = (EditText) findViewById(R.id.textbox7);
        signUpPasswordConfirm = (EditText) findViewById(R.id.textbox8);
        firstName = (EditText) findViewById(R.id.textbox4);
        lastName  = (EditText) findViewById(R.id.textbox5);
        phoneNumber = (EditText) findViewById(R.id.textbox6);
        progBar = (ProgressBar) findViewById(R.id.progressbar);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button4:
                startActivity(new Intent(this, DriverLogin.class));
                break;

            case R.id.button3:
                final String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                String passconfirm = signUpPasswordConfirm.getText().toString().trim();
                final String fname = firstName.getText().toString().trim();
                final String lname = lastName.getText().toString().trim();
                final String phone = phoneNumber.getText().toString().trim();

                if (email.isEmpty()){
                    signUpEmail.setError("Email is required");
                    signUpEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    signUpEmail.setError("Please enter a valid e-mail");
                    signUpEmail.requestFocus();
                    return;
                }

                if (fname.isEmpty()){
                    firstName.setError("First name is required");
                    firstName.requestFocus();
                    return;
                }

                if (lname.isEmpty()){
                    lastName.setError("Last name is required");
                    lastName.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    signUpPassword.setError("Password is required");
                    signUpPassword.requestFocus();
                    return;
                }

                if (password.length() < 6){
                    signUpPassword.setError("Minimum length of password is 6");
                    signUpPassword.requestFocus();
                    return;
                }
                if (!(passconfirm.equals(password))){
                    signUpPasswordConfirm.setError("Passwords should match");
                    signUpPasswordConfirm.requestFocus();
                    return;
                }

                progBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete (@NonNull Task<AuthResult> task){
                        progBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                            mAuth.getCurrentUser().sendEmailVerification();
                            User user = new User(fname, lname, email, phone);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);
                            startActivity(new Intent(SignUp.this, DriverLogin.class));


                        }
                        else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "User e-mail already registered", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



                break;
        }



    }
}
