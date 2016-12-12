package com.ge.digital.model;

/**
 * Created by benoitlaurent on 12/12/16.
 */

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}