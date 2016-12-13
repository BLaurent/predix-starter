package com.ge.digital.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by benoitlaurent on 13/12/16.
 */
public class DefaultResponse {
    @Getter
    @Setter
    private String ok;

    public DefaultResponse() {
        ok = "ok";
    }

    public DefaultResponse(String ok) {
        this.ok = ok;
    }

}
