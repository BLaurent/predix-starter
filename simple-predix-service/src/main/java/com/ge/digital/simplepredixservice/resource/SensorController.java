package com.ge.digital.simplepredixservice.resource;

import com.ge.digital.simplepredixservice.ingester.Ingester;
import com.ge.digital.simplepredixservice.model.PostSensorRequestBody;
import com.ge.predix.timeseries.client.ClientFactory;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.builder.QueryBuilder;
import com.ge.predix.timeseries.model.builder.QueryTag;
import com.ge.predix.timeseries.model.response.QueryResponse;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class SensorController extends RestApiResource {
    private final Ingester ingester;

    public SensorController(Ingester ingester) {
        this.ingester = ingester;
    }

    @ApiOperation(value="Ingest sensors data to Time Series")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token authorization retrieved from UAA.", required = true, dataType = "string", paramType = "header", example = "Bearer 68e34344e5-d485-466a-b55f-0324fe345rg3445"),
    })
    @PostMapping(path = "/sensor", consumes = "application/json", produces = "text/plain")
    String create(@RequestBody PostSensorRequestBody entity) throws Exception {
        Long time = entity.getSec() * 1000L + entity.getUsec() / 1000L;
        ingester.pushDataPoint(time,
                entity.getValue(),
                entity.getType() + '-' + entity.getDeviceId());
        return "OK";
    }

    @ApiOperation(value="Retreive the last n sensors data from Time Series")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token authorization retrieved from UAA.", required = true, dataType = "string", paramType = "header", example = "Bearer 68e34344e5-d485-466a-b55f-0324fe345rg3445"),
    })
    @GetMapping(path="/sensor", produces = "application/json")
    String read(@RequestParam("tag") String tag, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, PredixTimeSeriesException {
        QueryBuilder builder = null;
            builder = QueryBuilder.createQuery()
                    .withStartAbs(Long.parseLong(startDate))
                    .withEndAbs(Long.parseLong(endDate))
                    .addTags(
                            QueryTag.Builder.createQueryTag()
                                    .withTagNames(Arrays.asList(tag))
                                                   .build());

        QueryResponse response = null;
            response = ClientFactory.queryClientForTenant(ingester.getTenantContext()).queryAll(builder.build());

        Gson gson = new Gson();
        return gson.toJson(response);
    }
}
