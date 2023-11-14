package com.firstapp.ratingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private ReviewClickListener clickListener;

    public ReviewsAdapter(List<Review> reviewList, ReviewClickListener clickListener) {
        this.reviewList = reviewList;
        this.clickListener = clickListener;
    }

    public void addReview(Review review) {
        reviewList.add(review);
        notifyItemInserted(reviewList.size() - 1);
    }

    public void setReviews(List<Review> reviews) {
        reviewList.clear();
        reviewList.addAll(reviews);
        notifyDataSetChanged();
    }

    public void clearReviews() {
        reviewList.clear();
        notifyDataSetChanged();
    }

    public interface ReviewClickListener {
        void onReviewClick(Review review);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.reviewerUsernameTextView.setText(review.getReviewerName());
        holder.ratingTextView.setText(String.valueOf(review.getRating()));
        holder.commentTextView.setText(review.getComment());

        // Adaugă driverResponse în TextView-ul corespunzător
        if (review.getDriverResponse() != null && !review.getDriverResponse().isEmpty()) {
            holder.driverResponseTextView.setVisibility(View.VISIBLE);
            holder.driverResponseTextView.setText("Răspuns șofer: " + review.getDriverResponse());
        } else {
            holder.driverResponseTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerUsernameTextView;
        TextView ratingTextView;
        TextView commentTextView;
        TextView driverResponseTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerUsernameTextView = itemView.findViewById(R.id.usernameTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            commentTextView = itemView.findViewById(R.id.reviewTextView);
            driverResponseTextView = itemView.findViewById(R.id.driverResponseTextView);

            // Adaugă click listener pe întregul item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && clickListener != null) {
                        clickListener.onReviewClick(reviewList.get(position));
                    }
                }
            });
        }
    }
}
