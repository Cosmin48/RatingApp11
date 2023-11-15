package com.firstapp.ratingapp;

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

public class DriverProfileActivity extends AppCompatActivity {

    private EditText editDriverName;
    private EditText editDriverEmail;
    private EditText editDriverContact;
    private EditText editVehicleDetails;
    private EditText editLicensePlate;
    private Button saveDriverProfileButton;
    private Button editProfileButton;
    private Button viewMapButton;
    private Button viewReviewsButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String currentUserId;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        editDriverName = findViewById(R.id.editDriverName);
        editDriverEmail = findViewById(R.id.editDriverEmail);
        editDriverContact = findViewById(R.id.editDriverContact);
        editVehicleDetails = findViewById(R.id.editVehicleDetails);
        editLicensePlate = findViewById(R.id.editLicensePlate);
        saveDriverProfileButton = findViewById(R.id.saveDriverProfileButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        viewMapButton = findViewById(R.id.viewMapButton);
        viewReviewsButton = findViewById(R.id.viewReviewsButton);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(currentUserId);

        if (getIntent().hasExtra("editProfile") && getIntent().getBooleanExtra("editProfile", false)) {
            setEditMode(true);
        }

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadDriverProfile(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DriverProfileActivity.this, "Eroare la încărcarea profilului: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        saveDriverProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDriverProfile();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditMode(true);
            }
        });

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDriverMapActivity();
            }
        });

        viewReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviewsActivity();
            }
        });
    }

    private void loadDriverProfile(DataSnapshot dataSnapshot) {
        editDriverName.setText(dataSnapshot.child("Nume").getValue(String.class));
        editDriverEmail.setText(dataSnapshot.child("Email").getValue(String.class));
        editDriverContact.setText(dataSnapshot.child("Numar de telefon").getValue(String.class));
        editVehicleDetails.setText(dataSnapshot.child("Detalii vehicul").getValue(String.class));
        editLicensePlate.setText(dataSnapshot.child("Numar inmatriculare").getValue(String.class));
    }

    private void saveDriverProfile() {
        String driverName = editDriverName.getText().toString().trim();
        String driverEmail = editDriverEmail.getText().toString().trim();
        String driverContact = editDriverContact.getText().toString().trim();
        String vehicleDetails = editVehicleDetails.getText().toString().trim();
        String licensePlate = editLicensePlate.getText().toString().trim();

        if (driverName.isEmpty() || driverEmail.isEmpty() || driverContact.isEmpty() || vehicleDetails.isEmpty() || licensePlate.isEmpty()) {
            Toast.makeText(DriverProfileActivity.this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference driverRef = databaseRef;

            driverRef.child("Nume").setValue(driverName);
            driverRef.child("Email").setValue(driverEmail);
            driverRef.child("Numar de telefon").setValue(driverContact);
            driverRef.child("Detalii vehicul").setValue(vehicleDetails);
            driverRef.child("Numar inmatriculare").setValue(licensePlate);

            Toast.makeText(DriverProfileActivity.this, "Profilul șoferului a fost salvat cu succes", Toast.LENGTH_SHORT).show();
            setEditMode(false);
        }
    }

    private void setEditMode(boolean editMode) {
        this.editMode = editMode;

        // Activează sau dezactivează câmpurile în funcție de modul de editare
        editDriverName.setEnabled(editMode);
        editDriverEmail.setEnabled(editMode);
        editDriverContact.setEnabled(editMode);
        editVehicleDetails.setEnabled(editMode);
        editLicensePlate.setEnabled(editMode);

        // Afișează sau ascunde butoanele în funcție de modul de editare
        if (editMode) {
            saveDriverProfileButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.GONE);
        } else {
            saveDriverProfileButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);
        }
    }

    private void openDriverMapActivity() {
        Intent intent = new Intent(DriverProfileActivity.this, DriverMapActivity.class);
        startActivity(intent);
    }

    private void openReviewsActivity() {
        Intent intent = new Intent(DriverProfileActivity.this, ReviewsActivity.class);
        intent.putExtra("driverId", currentUserId);
        startActivity(intent);
    }
}
