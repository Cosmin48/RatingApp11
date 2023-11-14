package com.firstapp.ratingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firstapp.ratingapp.Review; // Importăm clasa Review
import com.google.firebase.database.ValueEventListener;

public class ReviewDriverActivity extends AppCompatActivity {

    private TextView driverNameTextView;
    private RatingBar driverRatingBar;
    private EditText reviewCommentEditText;
    private Button submitReviewButton;

    private FirebaseAuth mAuth;
    private DatabaseReference reviewsRef;
    private DatabaseReference driverReviewsRef;
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

        if (getIntent().hasExtra("driverId") && getIntent().hasExtra("driverName")) {
            driverId = getIntent().getStringExtra("driverId");
            driverName = getIntent().getStringExtra("driverName");
            driverNameTextView.setText(driverName);
        }

        driverReviewsRef = reviewsRef.child(driverId); // Referință la recenziile șoferului

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
            // Verificăm dacă utilizatorul a mai scris deja o recenzie pentru șofer
            driverReviewsRef.orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(ReviewDriverActivity.this, "Ați mai scris deja o recenzie pentru acest șofer.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Creăm o nouă recenzie cu datele introduse
                        Review review = new Review(currentUserId, driverId, rating, reviewComment);

                        // Generăm un ID unic pentru recenzie în baza de date
                        String reviewId = driverReviewsRef.push().getKey();

                        // Salvăm recenzia în baza de date sub ID-ul generat
                        driverReviewsRef.child(reviewId).setValue(review)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ReviewDriverActivity.this, "Recenzia a fost adăugată cu succes.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ReviewDriverActivity.this, "Eroare la adăugarea recenziei: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        // Redirecționăm utilizatorul către o altă activitate sau acțiune (exemplu: Lista șoferilor)
                                        Intent intent = new Intent(ReviewDriverActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReviewDriverActivity.this, "Eroare: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
