package com.ge.digital.simplepredixservice.resource;

import com.ge.digital.simplepredixservice.ingester.Ingester;
import com.ge.digital.simplepredixservice.model.PostSensorRequestBody;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
