package com.ge.digital;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;

/**
 * Created by benoitlaurent on 12/12/16.
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        log.debug("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames)
        {
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
