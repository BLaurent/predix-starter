package com.ge.digital.model;

/**
 * Created by benoitlaurent on 12/12/16.
 */
public class DashboardConfig {

    public DashboardConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public DashboardConfig() {

        defaultConfig = "POUET";
    }

    private String defaultConfig;

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }


}
