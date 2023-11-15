package com.firstapp.ratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements ReviewsAdapter.ReviewClickListener {

    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private EditText responseEditText;
    private Button submitResponseButton;
    private TextView averageRatingTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference reviewsRef;
    private DatabaseReference driverRef;
    private DatabaseReference userRef;

    private String currentUserId;
    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");
        driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        recyclerView = findViewById(R.id.recyclerView);
        responseEditText = findViewById(R.id.responseEditText);
        submitResponseButton = findViewById(R.id.submitResponseButton);
        averageRatingTextView = findViewById(R.id.averageRatingTextView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reviewsAdapter = new ReviewsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(reviewsAdapter);

        if (getIntent().hasExtra("driverId")) {
            driverId = getIntent().getStringExtra("driverId");
            loadReviews(driverId);
        }

        submitResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResponse();
            }
        });
    }

    private void loadReviews(String driverId) {
        DatabaseReference reviewsNode = driverRef.child(driverId).child("Reviews");

        Log.d("FirebaseData", "DriverId: " + driverId);

        reviewsNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot);

                List<Review> newReviews = new ArrayList<>();

                float totalRating = 0;
                int reviewCount = 0;

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseData", "ReviewSnapshot Key: " + reviewSnapshot.getKey());
                    Log.d("FirebaseData", "ReviewSnapshot Value: " + reviewSnapshot.getValue());

                    if (reviewSnapshot.getValue() instanceof Boolean) {
                        continue;
                    }

                    Review review = reviewSnapshot.getValue(Review.class);

                    if (review != null) {
                        newReviews.add(review);
                        totalRating += review.getRating();
                        reviewCount++;
                    }
                }

                if (reviewCount > 0) {
                    float averageRating = totalRating / reviewCount;
                    averageRatingTextView.setText("Rating mediu: " + averageRating);
                }

                reviewsAdapter.setReviews(newReviews);
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewsActivity.this, "Eroare la încărcarea recenziilor: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    @Override
    public void onReviewClick(Review review) {
        Intent intent = new Intent(ReviewsActivity.this, ReviewDetailsActivity.class);
        intent.putExtra("review", review);
        startActivity(intent);
    }

    private void submitResponse() {
        String responseText = responseEditText.getText().toString().trim();

        if (getIntent().hasExtra("driverId")) {
            driverId = getIntent().getStringExtra("driverId");

            DatabaseReference driverResponseRef = driverRef.child(driverId).child("DriverResponse");
            driverResponseRef.setValue(responseText);
        }

        responseEditText.setText("");
        Toast.makeText(this, "Răspunsul a fost salvat cu succes", Toast.LENGTH_SHORT).show();
    }
}
