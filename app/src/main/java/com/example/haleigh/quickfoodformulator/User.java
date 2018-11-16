package com.example.haleigh.quickfoodformulator;

import java.util.ArrayList;

public class User {
    private String first;
    private String last;
    private String email;

    ArrayList<String> foodList = new ArrayList<>();

    public User(String first, String last, String email, ArrayList<String> foodList) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.foodList = foodList;
    }


}
