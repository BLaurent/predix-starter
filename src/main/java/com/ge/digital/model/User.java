package com.ge.digital.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by benoitlaurent on 12/12/16.
 */

public class User {
    @Getter
    @Setter
    private String name;

    public User(String name) {
        this.name = name;
    }

}