package com.firstapp.ratingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private Context context;
    private String viewType;

    public ReviewsAdapter(List<Review> reviewList, Context context, String viewType) {
        this.reviewList = reviewList;
        this.context = context;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.commentTextView.setText(review.getComment());
        holder.ratingTextView.setText(String.valueOf(review.getRating()));
        holder.responseTextView.setText(review.getDriverResponse());
        holder.reviewerNameTextView.setText(review.getReviewerName());

        if ("Driver".equals(viewType)) {
            holder.driverResponseEditText.setVisibility(View.VISIBLE);
            holder.submitResponseButton.setVisibility(View.VISIBLE);
            holder.submitResponseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String response = holder.driverResponseEditText.getText().toString().trim();
                    if (!response.isEmpty()) {
                        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child("Drivers")
                                .child(review.getDriverId())
                                .child("Reviews")
                                .child(review.getReviewId());
                        reviewRef.child("driverResponse").setValue(response);

                        // Save response to the reviewer's profile
                        DatabaseReference reviewerReviewRef = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child("Customers")
                                .child(review.getReviewerId())
                                .child("Reviews")
                                .child(review.getReviewId());
                        reviewerReviewRef.child("driverResponse").setValue(response);
                    }
                }
            });
        } else {
            holder.driverResponseEditText.setVisibility(View.GONE);
            holder.submitResponseButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView commentTextView;
        TextView ratingTextView;
        TextView responseTextView;
        TextView reviewerNameTextView;
        EditText driverResponseEditText;
        Button submitResponseButton;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
            reviewerNameTextView = itemView.findViewById(R.id.reviewerNameTextView);
            driverResponseEditText = itemView.findViewById(R.id.driverResponseEditText);
            submitResponseButton = itemView.findViewById(R.id.submitResponseButton);
        }
    }
}
