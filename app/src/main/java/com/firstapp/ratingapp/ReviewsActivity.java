package com.firstapp.ratingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewList;
    private TextView averageRatingTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference reviewsRef;
    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        averageRatingTextView = findViewById(R.id.averageRatingTextView);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviewList, this, "Driver");
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        mAuth = FirebaseAuth.getInstance();
        driverId = getIntent().getStringExtra("driverId");
        reviewsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("Reviews");

        loadReviews();
    }

    private void loadReviews() {
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                float totalRating = 0;
                int reviewCount = 0;
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        reviewList.add(review);
                        totalRating += review.getRating();
                        reviewCount++;
                    }
                }
                if (reviewCount > 0) {
                    float averageRating = totalRating / reviewCount;
                    averageRatingTextView.setText("Rating mediu: " + String.format("%.1f", averageRating));
                } else {
                    averageRatingTextView.setText("Rating mediu: N/A");
                }
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewsActivity.this, "Eroare la încărcarea recenziilor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
