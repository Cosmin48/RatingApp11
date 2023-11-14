package com.firstapp.ratingapp;

import android.content.Intent;
import android.os.Bundle;
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

    private FirebaseAuth mAuth;
    private DatabaseReference driverRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(currentUserId);

        recyclerView = findViewById(R.id.recyclerView);
        responseEditText = findViewById(R.id.responseEditText);
        submitResponseButton = findViewById(R.id.submitResponseButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Inițializează adapterul pentru recenziile șoferului
        reviewsAdapter = new ReviewsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(reviewsAdapter);

        loadReviews(); // Funcție pentru încărcarea recenziilor din Firebase

        submitResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResponse();
            }
        });
    }

    private void loadReviews() {
        // Descarcă recenziile din Firebase și adaugă-le la adapterul recycler view
        driverRef.child("Reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Review> newReviews = new ArrayList<>();

                float totalRating = 0;
                int reviewCount = 0;

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    newReviews.add(review);

                    // Adună rating-ul pentru calculul mediei
                    totalRating += review.getRating();
                    reviewCount++;
                }

                // Calculul mediei și actualizarea TextView-ului
                if (reviewCount > 0) {
                    float averageRating = totalRating / reviewCount;
                    TextView averageRatingTextView = findViewById(R.id.averageRatingTextView);
                    averageRatingTextView.setText("Rating mediu: " + averageRating);
                }

                reviewsAdapter.setReviews(newReviews); // înlocuiește lista curentă cu cea nouă
                reviewsAdapter.notifyDataSetChanged(); // Notifică adapterul despre modificări
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewsActivity.this, "Eroare la încărcarea recenziilor: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onReviewClick(Review review) {
        // Deschide o nouă activitate pentru vizualizarea și răspunsul la recenzie
        Intent intent = new Intent(ReviewsActivity.this, ReviewDetailsActivity.class);
        intent.putExtra("review", review);
        startActivity(intent);
    }

    private void submitResponse() {
        // Obține textul răspunsului introdus de șofer
        String responseText = responseEditText.getText().toString().trim();

        // Actualizează răspunsul șoferului în Firebase
        driverRef.child("DriverResponse").setValue(responseText);

        // Resetează câmpul de introducere text și afișează un mesaj de succes
        responseEditText.setText("");
        Toast.makeText(this, "Răspunsul a fost salvat cu succes", Toast.LENGTH_SHORT).show();
    }
}
