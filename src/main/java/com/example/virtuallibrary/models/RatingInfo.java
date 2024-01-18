package com.example.virtuallibrary.models;

public class RatingInfo {

    private int wholeStars;
    private double fractionalPart;

    public RatingInfo(int wholeStars, double fractionalPart) {
        this.wholeStars = wholeStars;
        this.fractionalPart = fractionalPart;
    }

    public int getWholeStars() {
        return wholeStars;
    }

    public void setWholeStars(int wholeStars) {
        this.wholeStars = wholeStars;
    }

    public double getFractionalPart() {
        return fractionalPart;
    }

    public void setFractionalPart(double fractionalPart) {
        this.fractionalPart = fractionalPart;
    }
    
}
