package com.ugtechie.AndroidFirebaseEmailPassword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private Button buttonLogOut;
    private TextView textViewUserId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        buttonLogOut = findViewById(R.id.button_log_out);
        textViewUserId = findViewById(R.id.text_view_user_id);

        textViewUserId.setText(
                mAuth.getCurrentUser().getUid()
        );

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLogout();
            }
        });


    }

    private void startLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();


    }
}
