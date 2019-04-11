package com.example.bishoysefin.pharmd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Launch extends AppCompatActivity {
    Button mDriver, mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        mPatient = (Button) findViewById(R.id.patient);
        mDriver = (Button) findViewById(R.id.driver);

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Launch.this, DriverLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Launch.this, PatientLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
}
