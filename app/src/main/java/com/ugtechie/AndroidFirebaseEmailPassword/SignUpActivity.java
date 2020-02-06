package com.ugtechie.AndroidFirebaseEmailPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private String userId;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textViewLogin = findViewById(R.id.text_view_login);
        buttonRegister = findViewById(R.id.button_register);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adds a new user to Firebase
                SignUpNewUser();
            }
        });
    }

    private void SignUpNewUser() {

        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Validating the user input
        if (firstName.isEmpty()) {
            //Toast.makeText(this, "Please enter First name", Toast.LENGTH_SHORT).show();
            editTextFirstName.setError("First name is required");

        } else if (lastName.isEmpty()) {
            // Toast.makeText(this, "Please enter Last name", Toast.LENGTH_SHORT).show();
            editTextLastName.setError("Last name is required");
        } else if (email.isEmpty()) {
            //Toast.makeText(this, "An email is required", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email is required");
        } else if (password.isEmpty()) {
            //Toast.makeText(this, "A password is required", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("Password is required");
        } else if (password.length() < 8) {
            editTextPassword.setError("Password must be at least 8 characters");

        } else {

            //Initializing the progressDialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                //Sign in success
                                Log.d(TAG, "onComplete: createUSerWith Email successful");
                                FirebaseUser user = task.getResult().getUser();
                                userId = mAuth.getCurrentUser().getUid();

                                //Add profile data to Firestore users collection
                                CreateUserProfile(firstName, lastName, email, userId);

                                //Go to home activity
                                startHomeActivity(user);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Creating Account failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            //Consider adding an OnFailureListener(new)
        }
    }

    private void CreateUserProfile(String firstName, String lastName, String email, final String userId) {

        DocumentReference docRef = db.collection("Users").document(userId);

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);


        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: User profile creates  for " + userId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    private void startHomeActivity(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
