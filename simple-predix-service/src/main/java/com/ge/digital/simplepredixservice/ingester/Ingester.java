package com.ge.digital.simplepredixservice.ingester;

import com.ge.digital.simplepredixservice.utils.PredixUtil;
import com.ge.predix.timeseries.client.TenantContext;
import com.ge.predix.timeseries.client.TenantContextFactory;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by benoitlaurent on 22/08/16.
 */
@Slf4j
@Component
public class Ingester {

    @Value("${vcap.services.uaa-lab.credentials.issuerId}")
    private String uaaUri;

    @Value("${vcap.services.timeseries-lab.credentials.ingest.uri}")
    private String ingestUri;

    @Value("${vcap.services.timeseries-lab.credentials.query.uri}")
    private String queryUri;

    @Value("${vcap.services.timeseries-lab.credentials.ingest.zone-http-header-value}")
    private String tsZoneId;

    @Value("${tsClientId}")
    private String clientId;

    @Value("${tsClientSecret}")
    private String clientSecret;


    private TenantContext tenantContext;

    private Queues queue;

    @PostConstruct
    void init() {
        setupPredixProperties();

        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                envName,
                env.get(envName));
        }

        try {
            tenantContext = TenantContextFactory.createTenantContextFromPropertiesFile(
                "/predix-timeseries.properties");
        } catch (URISyntaxException | PredixTimeSeriesException | IOException e) {
            e.printStackTrace();
        }

        queue = new Queues();
        DataIngester ingest = new DataIngester(queue, tenantContext);

        new Timekeeper(new ThreadContainer[] {ingest}, PredixUtil.INGEST_RATE);

    }

    private void setupPredixProperties() {

        try {
            // create and set properties into properties object
            Properties props = new Properties();
            props.setProperty("predix.timeseries.maxTagsPerQuery", "5");
            props.setProperty("predix.timeseries.maxIngestionMessageSize", "512000");
            props.setProperty("plan.ingestion.concurrent.connections.max", "3");
            props.setProperty("plan.query.concurrent.connections.max", "100");
            props.setProperty("uaa.uri", uaaUri);
            props.setProperty("ingestion.uri", ingestUri);
            props.setProperty("ingestion.zone-http-header-name", "Predix-Zone-Id");
            props.setProperty("ingestion.zone-http-header-value", tsZoneId);
            props.setProperty("ingestion.client.id", clientId);
            props.setProperty("ingestion.client.secret", clientSecret);

            props.setProperty("query.uri", queryUri);
            props.setProperty("query.zone-http-header-name", "Predix-Zone-Id");
            props.setProperty("query.zone-http-header-value", tsZoneId);
            props.setProperty("query.client.id", clientId);
            props.setProperty("query.client.secret", clientSecret);
            props.setProperty("query.zone-http-header-name", "Predix-Zone-Id");

            // get or create the file
            File f = new File(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("predix-timeseries.properties"))
                    .toURI());
            OutputStream out = new FileOutputStream(f, false);
            // write into it
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(props, out, "\nUAA endpoint");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushDataPoint(Long timeStamp, double value, String tag) {
        queue.pushDataPoint(new TaggedDataPoint(tag, new DataPoint(timeStamp, value)));
    }

}
