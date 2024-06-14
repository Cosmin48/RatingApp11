package com.firstapp.ratingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewDriverActivity extends AppCompatActivity {

    private EditText commentEditText;
    private RatingBar ratingBar;
    private Button submitReviewButton;
    private DatabaseReference reviewsRef;
    private DatabaseReference responsesRef;
    private String driverId;

    private static final String TAG = "ReviewDriverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_driver);

        commentEditText = findViewById(R.id.commentEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        driverId = getIntent().getStringExtra("driverId");
        Log.d(TAG, "Driver ID: " + driverId);
        if (driverId == null) {
            Toast.makeText(this, "Driver ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reviewsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("Reviews");

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        String comment = commentEditText.getText().toString().trim();
        float rating = ratingBar.getRating();
        String reviewerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reviewerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); // Or wherever you get the name from

        if (comment.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        String reviewId = reviewsRef.push().getKey();
        if (reviewId == null) {
            Toast.makeText(this, "Failed to create review ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review(driverId, reviewId, comment, rating, reviewerId, reviewerName);
        reviewsRef.child(reviewId).setValue(review).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ReviewDriverActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ReviewDriverActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + task.getException().getMessage());
            }
        });
    }
}
