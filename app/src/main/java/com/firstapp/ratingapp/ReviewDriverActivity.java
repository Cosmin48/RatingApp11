package com.firstapp.ratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewDriverActivity extends AppCompatActivity {

    private TextView driverNameTextView;
    private RatingBar driverRatingBar;
    private EditText reviewCommentEditText;
    private Button submitReviewButton;

    private FirebaseAuth mAuth;
    private DatabaseReference reviewsRef;
    private DatabaseReference driverRef;
    private String currentUserId;
    private String driverId;
    private String driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_driver);

        driverNameTextView = findViewById(R.id.driverNameTextView);
        driverRatingBar = findViewById(R.id.driverRatingBar);
        reviewCommentEditText = findViewById(R.id.reviewCommentEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");
        driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

        if (getIntent().hasExtra("driverId") && getIntent().hasExtra("driverName")) {
            driverId = getIntent().getStringExtra("driverId");
            driverName = getIntent().getStringExtra("driverName");
            driverNameTextView.setText(driverName);
        }

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        float rating = driverRatingBar.getRating();
        String reviewComment = reviewCommentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(reviewComment)) {
            Toast.makeText(this, "Vă rugăm să lăsați un comentariu.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference driverReviewsRef = driverRef.child(driverId).child("Reviews");

            String reviewId = driverReviewsRef.push().getKey();
            Review review = new Review(currentUserId, driverId, rating, reviewComment);

            driverReviewsRef.child(reviewId).setValue(review)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Salvăm și rating-ul mediu al șoferului, pentru a-l putea afișa în profil
                                updateDriverRating(driverId);

                                Toast.makeText(ReviewDriverActivity.this, "Recenzia a fost adăugată cu succes.", Toast.LENGTH_SHORT).show();

                                // Redirecționăm utilizatorul către o altă activitate sau acțiune (exemplu: Lista șoferilor)
                                Intent intent = new Intent(ReviewDriverActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ReviewDriverActivity.this, "Eroare la adăugarea recenziei: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void updateDriverRating(String driverId) {
        DatabaseReference driverReviewsRef = reviewsRef.child(driverId);

        driverReviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                float totalRating = 0;
                int reviewCount = 0;

                for (com.google.firebase.database.DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        totalRating += review.getRating();
                        reviewCount++;
                    }
                }

                if (reviewCount > 0) {
                    float averageRating = totalRating / reviewCount;
                    driverRef.child(driverId).child("averageRating").setValue(averageRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tratează erorile dacă este necesar
            }
        });
    }
}
