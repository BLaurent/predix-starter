package com.ge.digital.simplepredixservice.resource;

import com.ge.digital.simplepredixservice.model.DashboardConfig;
import com.ge.digital.simplepredixservice.model.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController extends RestApiResource {
    @ApiOperation(value="Get the Dashboard content")
    @GetMapping(path="/dashboard")
    DashboardConfig getDashboard(User user){
        return new DashboardConfig();
    }

}