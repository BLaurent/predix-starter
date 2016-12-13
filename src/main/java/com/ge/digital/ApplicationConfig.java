package com.ge.digital;

import com.ge.digital.services.DashboardController;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.ge.digital.services", "com.ge.digital.config"})
public class ApplicationConfig {

    @Autowired
    private DashboardController dashboardController;

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean(destroyMethod = "destroy")
    public Server jaxRsServer() {
        final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setServiceBean(dashboardController);
        factory.setProvider(new JacksonJsonProvider());
        factory.setBus(cxf());
        factory.setAddress("/");
        return factory.create();
    }

    @Bean
    public ServletRegistrationBean cxfServlet() {
        final ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new CXFServlet(), "/api/v1/*");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }

}
