package com.firstapp.ratingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editContact;
    private Button saveButton;
    private Button editProfileButton;
    private Button selectDriverButton;
    //private Button reviewDriverButton;
    private boolean editMode = false;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String currentUserId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editContact = findViewById(R.id.editContact);
        saveButton = findViewById(R.id.saveButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        selectDriverButton = findViewById(R.id.selectDriverButton);
        //reviewDriverButton = findViewById(R.id.reviewDriverButton);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(currentUserId);

        if (getIntent().hasExtra("editProfile") && getIntent().getBooleanExtra("editProfile", false)) {
            editUsername.setEnabled(false);
            editEmail.setEnabled(false);
            editContact.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);
            editMode = true;
        }

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    editUsername.setText(dataSnapshot.child("Nume").getValue(String.class));
                    editEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                    editContact.setText(dataSnapshot.child("Numar de telefon").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Eroare la încărcarea profilului: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String contact = editContact.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(UserProfileActivity.this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference userRef = databaseRef;

                    userRef.child("Nume").setValue(username);
                    userRef.child("Email").setValue(email);
                    userRef.child("Numar de telefon").setValue(contact);

                    Toast.makeText(UserProfileActivity.this, "Profilul utilizatorului a fost salvat cu succes", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUsername.setEnabled(true);
                editEmail.setEnabled(true);
                editContact.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                editProfileButton.setVisibility(View.GONE);
                editMode = true;
            }
        });

        selectDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SelectDriverActivity.class);
                startActivity(intent);
            }
        });

       // reviewDriverButton.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
               // Intent intent = new Intent(UserProfileActivity.this, ReviewDriverActivity.class);
                //startActivity(intent);
          //  }
       // });

    }
}
