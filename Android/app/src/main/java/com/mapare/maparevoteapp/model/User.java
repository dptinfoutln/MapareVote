package com.mapare.maparevoteapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("email")
    private String email;
    @SerializedName("lastname")
    private String name;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("password")
    private String password;

    public User(String email, String name, String firstname, String password) {
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.password = password;
    }
}
