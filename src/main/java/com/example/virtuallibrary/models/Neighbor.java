package com.example.virtuallibrary.models;


public class Neighbor {

    private WordVector vector;
    private double distance;
    
    public Neighbor(WordVector vector, double distance) {
        this.vector = vector;
        this.distance = distance;
    }
    public WordVector getVector() {
        return vector;
    }
    public void setVector(WordVector vector) {
        this.vector = vector;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
}
