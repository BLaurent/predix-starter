package com.ge.digital.resource;

import com.ge.digital.model.DashboardConfig;
import com.ge.digital.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by benoitlaurent on 12/12/16.
 */
@RestController
public class DashboardController {

    @GetMapping("/dashboard")
    DashboardConfig getDashboard(@RequestParam(value = "user") User user){
        return new DashboardConfig();
    }
}
