package com.example.jj.oscar_reviews;

public class Review {
    private String date;
    private String reviewer;
    private String Category;
    private String Nominee;
    private String Review;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getNominee() {
        return Nominee;
    }

    public void setNominee(String nominee) {
        Nominee = nominee;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }
}
