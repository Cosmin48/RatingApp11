package com.firstapp.ratingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        saveButton = findViewById(R.id.saveButton);

        // Aici puteți prelua datele utilizatorului din profilul actual și le afișați în câmpurile de editare

        // Adăugați un listener pentru butonul de salvare
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aici puteți prelua datele din câmpurile de editare și le salvați în profilul utilizatorului
                // Apoi, puteți închide activitatea sau efectuați alte acțiuni necesare.
            }
        });
    }
}


