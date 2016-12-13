package com.ge.digital.services;

import com.ge.digital.model.DataPayload;
import com.ge.digital.model.DefaultResponse;
import com.ge.predix.timeseries.client.ClientFactory;
import com.ge.predix.timeseries.client.TenantContext;
import com.ge.predix.timeseries.client.TenantContextFactory;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.builder.IngestionRequestBuilder;
import com.ge.predix.timeseries.model.builder.IngestionTag;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import com.ge.predix.timeseries.model.datapoints.Quality;
import com.ge.predix.timeseries.model.response.IngestionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * Created by benoitlaurent on 13/12/16.
 */
@Service
@Path("ingest")
@Slf4j
public class DataIngester {

    @Autowired
    private TenantContext tenantContext;

    @POST
    @Consumes("application/json")
    @Produces({
            "application/json"
    })
    public DefaultResponse ingesterData(DataPayload data) {

        IngestionRequestBuilder ingestionBuilder = IngestionRequestBuilder.createIngestionRequest()
                .withMessageId(UUID.randomUUID().toString())
                .addIngestionTag(IngestionTag.Builder.createIngestionTag()
                        .withTagName(data.getTagName())
                        .addDataPoints(
                                Collections.singletonList(
                                        new DataPoint(new Date().getTime(), data.getValue(), Quality.GOOD)
                                )
                        )
                        .build());
        try {
            String json = ingestionBuilder.build().get(0);
            log.info(json);
            IngestionResponse response = ClientFactory.ingestionClientForTenant(tenantContext).ingest(json);
            String responseStr = response.getMessageId() + response.getStatusCode();
            log.info(responseStr);
            return new DefaultResponse(responseStr);
        } catch (IOException | PredixTimeSeriesException e) {
            e.printStackTrace();
            return new DefaultResponse(Arrays.toString(e.getStackTrace()));
        }
    }

    @Bean
    public TenantContext tenantContextFactory() throws IOException, PredixTimeSeriesException, URISyntaxException {
        return TenantContextFactory.createTenantContextFromPropertiesFile("/predix-timeseries");
    }
}
