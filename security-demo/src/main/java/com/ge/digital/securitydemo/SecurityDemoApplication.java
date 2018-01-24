package com.ge.digital.securitydemo;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;

@SpringBootApplication
@Log
public class SecurityDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SecurityDemoApplication.class, args);

        log.info("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.info(beanName);
        }

        log.info("Let's inspect the properties provided by Spring Boot:");
        MutablePropertySources propertySources = ctx.getEnvironment().getPropertySources();
        for (PropertySource<?> propertySourceObject : propertySources) {
            if (propertySourceObject != null) {
                log.info("propertySource=" + propertySourceObject.getName() + " values=" + propertySourceObject.getSource()
                        + "class=" + propertySourceObject.getClass());
            }
        }

        log.info("Let's inspect the profiles provided by Spring Boot:");
        String profiles[] = ctx.getEnvironment().getActiveProfiles();
        for (String profile : profiles) log.info("profile=" + profile);
    }
}

