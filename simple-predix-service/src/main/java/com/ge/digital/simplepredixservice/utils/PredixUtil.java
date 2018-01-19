package com.ge.digital.simplepredixservice.utils;

import lombok.extern.java.Log;

@Log
public class PredixUtil {


    private static String getEnv(String env) {
        return getEnv(env, false, "");
    }

    private static String getEnv(String env, String def) {
        return getEnv(env, true, def);
    }

    private static String getEnv(String env, boolean optional, String def) {
        String value = System.getenv(env);
        if (value != null) {
            log.info(env + ": " + value);
            return value;
        } else {
            if (optional) {
                log.info("Using default value for " + env + " of " + def);
                return def;
            } else {
                log.info(env + " is currently not set, system exiting");
                System.exit(0);
                return "";
            }
        }
    }

    private static int DEFAULT_INGEST_RATE = 1;
    private static float DEFAULT_SAMPLE_RATE = 20000.0f;
    private static int DEFAULT_POINTS_SECOND = 10;
    private static int DEFAULT_SAVE_INTERVAL = 20;
    private static double DEFAULT_NETWORK_RATE = 0.5;
    private static boolean DEFAULT_DELETE_FILES = true;
    public static int INGEST_RATE = Integer.parseInt(getEnv("INGEST_RATE", DEFAULT_INGEST_RATE + ""));

}



