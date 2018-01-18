package com.ge.digital.simplepredixservice.resource;

import com.ge.digital.simplepredixservice.model.DashboardConfig;
import com.ge.digital.simplepredixservice.model.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController extends RestApiResource {
    @ApiOperation(value="Get the Dashboard content")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token authorization retrieved from UAA.", required = true, dataType = "string", paramType = "header", example = "Bearer 68e34344e5-d485-466a-b55f-0324fe345rg3445"),
    })
    @GetMapping(path="/dashboard")
    DashboardConfig getDashboard(User user){
        return new DashboardConfig();
    }

}