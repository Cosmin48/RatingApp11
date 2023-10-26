package com.firstapp.ratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editContact;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editContact = findViewById(R.id.editContact);
        saveButton = findViewById(R.id.saveButton);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String contact = editContact.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                    // Cazul în care unul dintre câmpuri este necompletat
                    Toast.makeText(UserProfileActivity.this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                } else {
                    // Toate câmpurile sunt completate
                    // Salvați datele utilizatorului în Firebase Realtime Database
                    DatabaseReference userRef = databaseRef.child("Users").child("Customers").child(currentUserId);

                    // Setați valorile sub nodurile corespunzătoare
                    userRef.child("Nume").setValue(username);
                    userRef.child("Email").setValue(email);
                    userRef.child("Numar de telefon").setValue(contact);

                    // Procesul de salvare a profilului a fost realizat cu succes
                    Toast.makeText(UserProfileActivity.this, "Profil salvat cu succes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
