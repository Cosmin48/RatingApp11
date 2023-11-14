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
    private Button editProfileButton; // Buton pentru editare profil
    private boolean editMode = false; // Mod de editare, inițial dezactivat
    private Button viewMapButton; // Butonul pentru a deschide harta
    private Button viewReviewsButton; // Buton pentru a vizualiza recenziile

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String currentUserId;

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
        editProfileButton = findViewById(R.id.editProfileButton); // Inițializarea butonului pentru editare profil
        viewMapButton = findViewById(R.id.viewMapButton); // Inițializarea butonului pentru harta
        viewReviewsButton = findViewById(R.id.viewReviewsButton); // Inițializarea butonului pentru recenzii

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(currentUserId);

        // Verificăm dacă modul de editare este activat
        if (getIntent().hasExtra("editProfile") && getIntent().getBooleanExtra("editProfile", false)) {
            // Mod de editare activat, dezactivăm câmpurile
            editDriverName.setEnabled(false);
            editDriverEmail.setEnabled(false);
            editDriverContact.setEnabled(false);
            editVehicleDetails.setEnabled(false);
            editLicensePlate.setEnabled(false);
            saveDriverProfileButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);
            editMode = true;
        }

        // Încărcăm profilul șoferului dacă există
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Profilul există, încărcăm datele în câmpuri
                    editDriverName.setText(dataSnapshot.child("Nume").getValue(String.class));
                    editDriverEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                    editDriverContact.setText(dataSnapshot.child("Numar de telefon").getValue(String.class));
                    editVehicleDetails.setText(dataSnapshot.child("Detalii vehicul").getValue(String.class));
                    editLicensePlate.setText(dataSnapshot.child("Numar inmatriculare").getValue(String.class));
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
                // Implementarea salvării profilului (codul rămas neschimbat)
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
                }
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activăm modul de editare, câmpurile devin editabile
                editDriverName.setEnabled(true);
                editDriverEmail.setEnabled(true);
                editDriverContact.setEnabled(true);
                editVehicleDetails.setEnabled(true);
                editLicensePlate.setEnabled(true);
                saveDriverProfileButton.setVisibility(View.VISIBLE);
                editProfileButton.setVisibility(View.GONE);
                editMode = true;
            }
        });

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide DriverMapActivity atunci când butonul este apăsat
                Intent intent = new Intent(DriverProfileActivity.this, DriverMapActivity.class);
                startActivity(intent);
            }
        });

        viewReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementează aici codul pentru a deschide activitatea pentru vizualizarea recenziilor
                // Exemplu:
                Intent intent = new Intent(DriverProfileActivity.this, ReviewsActivity.class);
                startActivity(intent);
            }
        });
    }
}
