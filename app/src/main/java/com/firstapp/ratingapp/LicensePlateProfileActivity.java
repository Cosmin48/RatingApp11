package com.firstapp.ratingapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LicensePlateProfileActivity extends AppCompatActivity {

    private TextView licensePlateTextView, reviewsTextView;
    private DatabaseReference licensePlatesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_plate_profile);

        licensePlateTextView = findViewById(R.id.licensePlateTextView);
        reviewsTextView = findViewById(R.id.reviewsTextView);
        licensePlatesRef = FirebaseDatabase.getInstance().getReference().child("LicensePlates");

        String licensePlate = getIntent().getStringExtra("licensePlate");
        licensePlateTextView.setText(licensePlate);

        licensePlatesRef.child(licensePlate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuilder reviews = new StringBuilder();
                    for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                        Review review = reviewSnapshot.getValue(Review.class);
                        reviews.append("Rating: ").append(review.getRating()).append("\n");
                        reviews.append("Comment: ").append(review.getComment()).append("\n");
                        reviews.append("Response: ").append(review.getDriverResponse()).append("\n\n");
                    }
                    reviewsTextView.setText(reviews.toString());
                } else {
                    reviewsTextView.setText("Nu există recenzii pentru acest număr de înmatriculare.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                reviewsTextView.setText("Eroare la încărcarea recenziilor.");
            }
        });
    }
}
