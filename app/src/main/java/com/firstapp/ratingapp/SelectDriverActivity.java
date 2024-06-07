package com.firstapp.ratingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectDriverActivity extends AppCompatActivity {

    private EditText licensePlateEditText;
    private Button searchButton;
    private DatabaseReference driversRef;

    private static final String TAG = "SelectDriverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);

        licensePlateEditText = findViewById(R.id.licensePlateEditText);
        searchButton = findViewById(R.id.searchButton);
        driversRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDriver();
            }
        });
    }

    private void searchDriver() {
        String licensePlate = licensePlateEditText.getText().toString().trim();

        if (licensePlate.isEmpty()) {
            Toast.makeText(this, "Introduceți un număr de înmatriculare", Toast.LENGTH_SHORT).show();
            return;
        }

        Query query = driversRef.orderByChild("Numar inmatriculare").equalTo(licensePlate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "DriverSnapshot: " + driverSnapshot.toString());
                        String driverId = driverSnapshot.getKey();
                        Log.d(TAG, "Driver ID: " + driverId);
                        Intent intent = new Intent(SelectDriverActivity.this, ReviewDriverActivity.class);
                        intent.putExtra("driverId", driverId);
                        intent.putExtra("licensePlate", licensePlate);
                        startActivity(intent);
                        return;
                    }
                } else {
                    Toast.makeText(SelectDriverActivity.this, "Șoferul nu a fost găsit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Eroare la căutarea șoferului: " + databaseError.getMessage());
                Toast.makeText(SelectDriverActivity.this, "Eroare la căutarea șoferului", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
