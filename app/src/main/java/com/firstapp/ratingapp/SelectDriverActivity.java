package com.firstapp.ratingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectDriverActivity extends AppCompatActivity {

    private EditText driverLicensePlate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);

        driverLicensePlate = findViewById(R.id.driverLicensePlate);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String licensePlate = driverLicensePlate.getText().toString();
                findDriverByLicensePlate(licensePlate);
            }
        });
    }

    private void findDriverByLicensePlate(String licensePlate) {
        DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        driversRef.orderByChild("Numar inmatriculare").equalTo(licensePlate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                                // Aici, driverSnapshot conține detaliile șoferului găsit.
                                // Exemplu pentru a obține numele șoferului:
                                String driverName = driverSnapshot.child("Nume").getValue(String.class);

                                // Păstrează ID-ul șoferului pentru a-l trimite către ReviewDriverActivity
                                String driverId = driverSnapshot.getKey();

                                // Treci la ReviewDriverActivity și trimite ID-ul șoferului și numele
                                Intent intent = new Intent(SelectDriverActivity.this, ReviewDriverActivity.class);
                                intent.putExtra("driverId", driverId);
                                intent.putExtra("driverName", driverName);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            showToast("Nu s-a găsit niciun șofer cu acest număr de înmatriculare.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showToast("Eroare la căutarea șoferului: " + databaseError.getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(SelectDriverActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
