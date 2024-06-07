package com.firstapp.ratingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewDetailsActivity extends AppCompatActivity {

    private TextView commentTextView;
    private TextView ratingTextView;
    private EditText driverResponseEditText;
    private Button submitResponseButton;

    private DatabaseReference reviewRef;
    private DatabaseReference userReviewRef;
    private String reviewId;
    private String reviewerId;
    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        commentTextView = findViewById(R.id.commentTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        driverResponseEditText = findViewById(R.id.driverResponseEditText);
        submitResponseButton = findViewById(R.id.submitResponseButton);

        reviewId = getIntent().getStringExtra("reviewId");
        reviewerId = getIntent().getStringExtra("reviewerId");
        driverId = getIntent().getStringExtra("driverId");

        reviewRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("Reviews").child(reviewId);
        userReviewRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(reviewerId).child("Reviews").child(reviewId);

        loadReviewDetails();

        submitResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResponse();
            }
        });
    }

    private void loadReviewDetails() {
        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String comment = dataSnapshot.child("comment").getValue(String.class);
                    double rating = dataSnapshot.child("rating").getValue(Double.class);
                    String driverResponse = dataSnapshot.child("driverResponse").getValue(String.class);

                    commentTextView.setText(comment);
                    ratingTextView.setText(String.valueOf(rating));
                    driverResponseEditText.setText(driverResponse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewDetailsActivity.this, "Eroare la încărcarea recenziei", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitResponse() {
        String driverResponse = driverResponseEditText.getText().toString().trim();

        if (driverResponse.isEmpty()) {
            Toast.makeText(this, "Completați răspunsul", Toast.LENGTH_SHORT).show();
            return;
        }

        reviewRef.child("driverResponse").setValue(driverResponse);
        userReviewRef.child("driverResponse").setValue(driverResponse);

        Toast.makeText(ReviewDetailsActivity.this, "Răspunsul a fost trimis", Toast.LENGTH_SHORT).show();
        finish();
    }
}
