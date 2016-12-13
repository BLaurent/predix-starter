package com.ge.digital.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by benoitlaurent on 12/12/16.
 */

public class DashboardConfig {

    @Getter
    @Setter
    private String defaultConfig;

    public DashboardConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public DashboardConfig() {

        defaultConfig = "POUET";
    }


}
