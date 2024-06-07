package com.firstapp.ratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private RecyclerView userReviewsRecyclerView;
    private Button selectDriverButton;
    private Button editProfileButton;
    private Button saveProfileButton;
    private Button logoutButton;

    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private List<String> driverResponsesList;
    private ResponsesAdapter responsesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);
        userReviewsRecyclerView = findViewById(R.id.userReviewsRecyclerView);
        selectDriverButton = findViewById(R.id.selectDriverButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        logoutButton = findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(currentUserId);

        driverResponsesList = new ArrayList<>();
        responsesAdapter = new ResponsesAdapter(driverResponsesList);

        userReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userReviewsRecyclerView.setAdapter(responsesAdapter);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadUserProfile(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        selectDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SelectDriverActivity.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditProfile(true);
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserProfile(DataSnapshot dataSnapshot) {
        nameEditText.setText(dataSnapshot.child("Nume").getValue(String.class));
        emailEditText.setText(dataSnapshot.child("Email").getValue(String.class));
        phoneEditText.setText(dataSnapshot.child("Numar de telefon").getValue(String.class));

        // Load driver responses from reviews
        driverResponsesList.clear();
        for (DataSnapshot reviewSnapshot : dataSnapshot.child("Reviews").getChildren()) {
            String driverResponse = reviewSnapshot.child("driverResponse").getValue(String.class);
            if (driverResponse != null && !driverResponse.isEmpty()) {
                driverResponsesList.add(driverResponse);
            }
        }
        responsesAdapter.notifyDataSetChanged();
    }

    private void enableEditProfile(boolean enable) {
        nameEditText.setEnabled(enable);
        emailEditText.setEnabled(enable);
        phoneEditText.setEnabled(enable);
        saveProfileButton.setVisibility(enable ? View.VISIBLE : View.GONE);
        editProfileButton.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            // Handle validation error
            return;
        }

        userRef.child("Nume").setValue(name);
        userRef.child("Email").setValue(email);
        userRef.child("Numar de telefon").setValue(phone);
        enableEditProfile(false);
    }
}
