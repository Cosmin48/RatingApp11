package com.firstapp.ratingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);

        mLogin = findViewById(R.id.loginButton);
        mRegistration = findViewById(R.id.registrationButton);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(DriverLoginActivity.this, "Completarea tuturor câmpurilor este necesară", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(DriverLoginActivity.this, "Parola trebuie să aibă cel puțin 6 caractere", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                        current_user_db.setValue(true);
                                        Toast.makeText(DriverLoginActivity.this, "Înregistrare cu succes", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DriverLoginActivity.this, "Eroare la înregistrare: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(DriverLoginActivity.this, "Completarea tuturor câmpurilor este necesară", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(DriverLoginActivity.this, DriverProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(DriverLoginActivity.this, "Eroare la autentificare: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
