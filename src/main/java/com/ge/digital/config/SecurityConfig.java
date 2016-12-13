package com.ge.digital.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Created by benoitlaurent on 12/12/16.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configure Spring Security
     *
     * @param http http security instance to be configured
     * @throws Exception if error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous().disable()
                .addFilterAfter(securityTokenFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .authorizeRequests()
                .antMatchers("/api/**")
                .access("isFullyAuthenticated()");
    }


    /**
     * @return {@link OAuth2AuthenticationProcessingFilter} used by Spring security to filter requests
     */
    private OAuth2AuthenticationProcessingFilter securityTokenFilter() {
        OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
        oauthAuthenticationManager.setTokenServices(tokenServices());
        OAuth2AuthenticationProcessingFilter filter = new OAuth2AuthenticationProcessingFilter();
        filter.setAuthenticationManager(oauthAuthenticationManager);
        filter.setAuthenticationEntryPoint(new OAuth2AuthenticationEntryPoint());
        return filter;
    }

    /**
     * @return {@link ResourceServerTokenServices} used by filter to access token service
     */
//    @Bean
//    @ConfigurationProperties(prefix = "uaa.tokenservices")
//    public ResourceServerTokenServices tokenServices() {
//        ResourceServerTokenServices fastTokenServices =  new FastTokenServices();
//        return fastTokenServices;
//    }

    @Bean
    @ConfigurationProperties(prefix = "auth.server")
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        return tokenServices;
    }
}

