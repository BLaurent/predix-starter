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

package com.ge.digital.simplepredixservice.ingester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.predix.timeseries.client.ClientFactory;
import com.ge.predix.timeseries.client.TenantContext;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.builder.IngestionRequestBuilder;
import com.ge.predix.timeseries.model.builder.IngestionTag;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Ingester Class Class to ingest data to the TS API
 */
@Slf4j
class DataIngester extends ThreadContainer implements Runnable {

    private Queues queue;

    public void setTenant(TenantContext tenant) {
        this.tenant = tenant;
    }

    private TenantContext tenant;
    private final int MAX_INGEST_SIZE = 1000;
    private Map<String, ArrayList<DataPoint>> dataPoints;

    /**
     * This class is for ingesting data points for a set tag Data points are
     * cretaed another thread class and added to the concurent queue in the
     * queue object.
     *
     * @param queue:  The cross thread queue object
     * @param tenant: the tenet for ingesting data
     */
    DataIngester(Queues queue, TenantContext tenant) {
        super();
        this.queue = queue;
        this.tenant = tenant;
        this.dataPoints = new HashMap<>();
        t = new Thread(this, "Ingester");
        t.start();
    }

    /**
     * This is the main method of the thread The datapoint array is cleared and
     * the thread sleeps for ingest rate to aloow it to fill. The thread wakes
     * up by the TK an pulls all points off the queue and Ingest them using the
     * java SDK
     */
    public void run() {
        int totalDatapoints;
        TaggedDataPoint dataPoint;
        ObjectMapper mapper = new ObjectMapper();

        while (true) {
            try {
                synchronized (t) {
                    t.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            totalDatapoints = 0;
            // Build dict of all Tag->Datapoint
            while (queue.getPointQueueSize() > 0 && totalDatapoints < MAX_INGEST_SIZE) {
                dataPoint = queue.popDataPoint();
                if (dataPoints.containsKey(dataPoint.getTag())) {
                    dataPoints.get(dataPoint.getTag()).add(dataPoint.getDataPoint());
                } else {
                    dataPoints.put(dataPoint.getTag(), new ArrayList<>());
                    dataPoints.get(dataPoint.getTag()).add(dataPoint.getDataPoint());
                }
                totalDatapoints++;
            }

            if(totalDatapoints > 0) {
                IngestionRequestBuilder ingestionBuilder = IngestionRequestBuilder.createIngestionRequest()
                        .withMessageId(timestamp);

                for (String tag : dataPoints.keySet()) {
                    ingestionBuilder.addIngestionTag(IngestionTag.Builder.createIngestionTag().withTagName(tag)
                            .addDataPoints(dataPoints.get(tag)).build());
                }
                log.debug("Ingesting " + totalDatapoints + " datapoints");
                String json = "";
                try {
                    json = ingestionBuilder.build().get(0);
                    log.debug(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ClientFactory.ingestionClientForTenant(tenant).ingest(json)));
                } catch (PredixTimeSeriesException e) {
                    log.error(
                            "Ingestion  Failed @ " + timestamp + " because  of HTTP connection. retrying once more");
                    try {
                        log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ClientFactory.ingestionClientForTenant(tenant).ingest(json)));
                    } catch ( PredixTimeSeriesException e1) {
                        log.error("Failed again, printing exception");
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataPoints.clear();
            }
        }
    }
}
