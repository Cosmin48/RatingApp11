package com.firstapp.ratingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String reviewerName;
    private String reviewerId;
    private String driverId;
    private float rating;
    private String comment;
    private String driverResponse;

    public Review() {
        // Constructor gol necesar pentru Firebase
    }

    // Constructor pentru recenzie
    public Review(String reviewerId, String driverId, float rating, String comment) {
        this.reviewerId = reviewerId;
        this.driverId = driverId;
        this.rating = rating;
        this.comment = comment;
    }

    // Constructor pentru recenzie cu raspunsul soferului
    public Review(String reviewerId, String driverId, float rating, String comment, String driverResponse) {
        this.reviewerId = reviewerId;
        this.driverId = driverId;
        this.rating = rating;
        this.comment = comment;
        this.driverResponse = driverResponse;
    }

    // Constructor pentru recenzie cu nume și text
    public Review(String reviewerName, String reviewText, String driverResponse) {
        this.reviewerName = reviewerName;
        this.comment = reviewText;
        this.driverResponse = driverResponse;
    }

    // Getteri și setteri pentru campurile din clasa

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDriverResponse() {
        return driverResponse;
    }

    public void setDriverResponse(String driverResponse) {
        this.driverResponse = driverResponse;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    // Parcelable implementation
    protected Review(Parcel in) {
        reviewerName = in.readString();
        reviewerId = in.readString();
        driverId = in.readString();
        rating = in.readFloat();
        comment = in.readString();
        driverResponse = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewerName);
        dest.writeString(reviewerId);
        dest.writeString(driverId);
        dest.writeFloat(rating);
        dest.writeString(comment);
        dest.writeString(driverResponse);
    }
}
