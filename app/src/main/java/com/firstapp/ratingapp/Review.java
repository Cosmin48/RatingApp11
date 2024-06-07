package com.firstapp.ratingapp;

public class Review {

    private String driverId;
    private String reviewId;
    private String comment;
    private float rating;
    private String driverResponse;
    private String reviewerId;
    private String reviewerName;

    // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    public Review() {
    }

    public Review(String driverId, String reviewId, String comment, float rating, String reviewerId, String reviewerName) {
        this.driverId = driverId;
        this.reviewId = reviewId;
        this.comment = comment;
        this.rating = rating;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
    }

    // Getters and setters
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDriverResponse() {
        return driverResponse;
    }

    public void setDriverResponse(String driverResponse) {
        this.driverResponse = driverResponse;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
}
