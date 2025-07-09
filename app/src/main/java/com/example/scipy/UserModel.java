package com.example.scipy;
public class UserModel {
    public String name, email, contact;

    public UserModel() {
        // Required for Firebase
    }

    public UserModel(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }
}

