package com.firstapp.ratingapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        TextView reviewerTextView = findViewById(R.id.reviewerTextView);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView commentTextView = findViewById(R.id.commentTextView);
        TextView responseTextView = findViewById(R.id.responseTextView);

        Review review = getIntent().getParcelableExtra("review");

        if (review != null) {
            reviewerTextView.setText("Reviewer: " + review.getReviewerName());
            ratingTextView.setText("Rating: " + review.getRating());
            commentTextView.setText("Comment: " + review.getComment());
            responseTextView.setText("Driver Response: " + review.getDriverResponse());
        }
    }
}
