package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("email")
    private String email;

    @JsonProperty("lastname")
    private String name;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("password")
    private String password;

    public User(String email, String name, String firstname, String password) {
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.password = password;
    }
}
