package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("email")
    private final String email;

    @JsonProperty("lastname")
    private final String name;

    @JsonProperty("firstname")
    private final String firstname;

    @JsonProperty("password")
    private final String password;

    public User(String email, String name, String firstname, String password) {
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.password = password;
    }
}
