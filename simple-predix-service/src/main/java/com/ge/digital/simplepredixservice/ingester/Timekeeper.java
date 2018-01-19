package com.ge.digital.simplepredixservice.ingester;

/*******************************************************************************
 * Copyright 2016 General Electric Company.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import java.util.Date;

/**
 * Timekeeper class Wakes up attached threads perioradicly
 */
class Timekeeper implements Runnable {
    private ThreadContainer[] threads;
    private double rate;

    /**
     * Timekeeper Constructor
     *
     * @param threads : list of threads to wake up
     * @param rate    :rate (seconds) they should wake up
     */
    Timekeeper(ThreadContainer[] threads, double rate) {
        this.threads = threads;
        this.rate = rate;

        new Thread(this).start();
    }

    /**
     * Main loop of thread Starts by waiting till the nearest whole multiple of
     * rate Then will notify threads at rate
     */
    public void run() {
        long ts;

        ts = new Date().getTime();

        try {
            if (rate >= 1)
                Thread.sleep((long) ((long) rate * 1000 - (ts % (rate * 1000))));
            else// sleep to the nearest second
            {
                Thread.sleep(((long) 1000 - (ts % (1000))));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Starting TK at rate: " + rate);
        while (true) {
            ts = new Date().getTime();
            ts = (long) (ts - ts % (rate * 1000));
            for (ThreadContainer t : threads) {
                if (t.getThread() != null) {
                    t.setTimestamp(ts + "");
                    synchronized (t.getThread()) {

                        t.getThread().notify();
                    }
                }
            }
            try {
                Thread.sleep((long) (rate * 1000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
