package com.ge.digital.services;

import com.ge.digital.model.DashboardConfig;
import com.ge.digital.model.User;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created by benoitlaurent on 12/12/16.
 */
@Path("dashboard")
@Service
public class DashboardController {

    @GET
    @Produces({
            "application/json"
    })
    public DashboardConfig getDashboard(@QueryParam("user") User user){
        return new DashboardConfig();
    }
}
