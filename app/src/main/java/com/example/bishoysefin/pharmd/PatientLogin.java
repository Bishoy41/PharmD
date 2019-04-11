package com.example.bishoysefin.pharmd;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PatientLogin extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText userEmail, userPassword;
    ProgressBar progBar;
    TextView textboxforgot;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlogin);
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.textboxforgot).setOnClickListener(this);

        userEmail = (EditText) findViewById(R.id.textbox1);
        userPassword = (EditText) findViewById(R.id.textbox2);
        progBar = (ProgressBar) findViewById(R.id.progressbar1);
        textboxforgot = (TextView) findViewById(R.id.textboxforgot);

        textboxforgot.setPaintFlags(textboxforgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }


    private void userLogin(String email, String password) {

        progBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    startActivity(new Intent(PatientLogin.this, PatientMap.class));
                    finish();
                    return;

                } else {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    userEmail.setError("Email is required");
                    userEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("Please enter a valid e-mail");
                    userEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    userPassword.setError("Password is required");
                    userPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    userPassword.setError("Minimum length of password is 6");
                    userPassword.requestFocus();
                    return;
                }

                userLogin(email, password);

                break;


            case R.id.button2:
                startActivity(new Intent(PatientLogin.this, PatientSignUp.class));

                break;

            case R.id.textboxforgot:
                email = userEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    userEmail.setError("Email is required");
                    userEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("Please enter a valid e-mail");
                    userEmail.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;

        }


    }

}
