package com.ge.digital.securitydemo.resource;

import com.ge.digital.securitydemo.model.DashboardConfig;
import com.ge.digital.securitydemo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping(path="/dashboard")
    DashboardConfig getDashboard(User user){
        return new DashboardConfig();
    }

    @RequestMapping("/")
    public String index( ) {
        return "index";
    }
}