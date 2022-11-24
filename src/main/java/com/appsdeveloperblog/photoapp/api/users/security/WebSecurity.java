/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.security;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)  
public class WebSecurity {

    private final Environment environment;
    private final UsersService usersService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment environment,
            UsersService usersService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
     
        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/actuator/**").permitAll()

                .anyRequest().authenticated().and()
                .addFilter(getAuthenticationFilter(authenticationManager))
                .addFilter(new AuthorizationFilter(authenticationManager, environment))
                .authenticationManager(authenticationManager)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        return http.build();
        
  
 
    }
 
    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(environment, authenticationManager, usersService);
        filter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
        return filter;
    }
    

}
