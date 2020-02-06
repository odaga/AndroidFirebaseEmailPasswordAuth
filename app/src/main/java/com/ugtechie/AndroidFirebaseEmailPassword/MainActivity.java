package com.ugtechie.AndroidFirebaseEmailPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button buttonGetStarted;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonGetStarted = findViewById(R.id.button_get_started);

        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user already signed in
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
