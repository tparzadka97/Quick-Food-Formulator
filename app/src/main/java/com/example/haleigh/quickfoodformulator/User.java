package com.example.haleigh.quickfoodformulator;

import java.util.ArrayList;

public class User {
    //fields of the user and constructor

    public String first;
    public String last;
    public String email;
    public ArrayList<String> foodList = new ArrayList<>();

    public User(String first, String last, String email, ArrayList<String> foodList) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.foodList = foodList;
    }


}
