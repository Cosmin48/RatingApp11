package com.firstapp.ratingapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverProfileActivity extends AppCompatActivity {

    private EditText editDriverName;
    private EditText editDriverEmail;
    private EditText editDriverContact;
    private EditText editVehicleDetails;
    private EditText editLicensePlate;
    private Button saveDriverProfileButton;
     private Button viewMapButton; // Butonul pentru a deschide harta

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
         viewMapButton = findViewById(R.id.viewMapButton); // Inițializarea butonului pentru harta

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        saveDriverProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverName = editDriverName.getText().toString().trim();
                String driverEmail = editDriverEmail.getText().toString().trim();
                String driverContact = editDriverContact.getText().toString().trim();
                String vehicleDetails = editVehicleDetails.getText().toString().trim();
                String licensePlate = editLicensePlate.getText().toString().trim();

                if (driverName.isEmpty() || driverEmail.isEmpty() || driverContact.isEmpty() || vehicleDetails.isEmpty() || licensePlate.isEmpty()) {
                    // Cazul în care unul dintre câmpuri este necompletat
                    Toast.makeText(DriverProfileActivity.this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                } else {
                    // Toate câmpurile sunt completate
                    // Salvați datele șoferului în Firebase Realtime Database
                    DatabaseReference driverRef = databaseRef.child("Users").child("Drivers").child(currentUserId);

                    // Setăm valorile sub nodurile corespunzătoare
                    driverRef.child("Nume").setValue(driverName);
                    driverRef.child("Email").setValue(driverEmail);
                    driverRef.child("Numar de telefon").setValue(driverContact);
                    driverRef.child("Detalii vehicul").setValue(vehicleDetails);
                    driverRef.child("Numar inmatriculare").setValue(licensePlate);

                    // Procesul de salvare a profilului șoferului a fost realizat cu succes
                    Toast.makeText(DriverProfileActivity.this, "Profilul șoferului a fost salvat cu succes", Toast.LENGTH_SHORT).show();
                }
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


    }
}
