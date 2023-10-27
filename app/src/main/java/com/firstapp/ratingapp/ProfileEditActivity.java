package com.firstapp.ratingapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextContact;
    private EditText editTextVehicleDetails;
    private EditText editTextLicensePlate;
    private Button saveProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Inițializarea elementelor vizuale pentru editare
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContact = findViewById(R.id.editTextContact);
        editTextVehicleDetails = findViewById(R.id.editTextVehicleDetails);
        editTextLicensePlate = findViewById(R.id.editTextLicensePlate);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        // Aici puteți încărca datele profilului existent din baza de date pentru a le afișa în câmpurile de editare

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aici puteți obține datele introduse în câmpurile de editare și să le salvați în baza de date
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String contact = editTextContact.getText().toString();
                String vehicleDetails = editTextVehicleDetails.getText().toString();
                String licensePlate = editTextLicensePlate.getText().toString();

                // Aici puteți efectua salvarea datelor în baza de date, de exemplu, în Firebase Realtime Database

                // După salvare, reveniți la activitatea de vizualizare a profilului
                Intent intent = new Intent(ProfileEditActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
