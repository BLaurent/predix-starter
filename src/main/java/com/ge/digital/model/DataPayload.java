package com.ge.digital.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by benoitlaurent on 13/12/16.
 */
public class DataPayload {

    @Getter
    @Setter
    private String tagName;

    @Getter
    @Setter
    private float value;

    public DataPayload() {
    }

    public DataPayload(String tagName, float value) {
        this.tagName = tagName;
        this.value = value;
    }

}